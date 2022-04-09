package developer.mihailzharkovskiy.stepcounter.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.TickerUseCase
import developer.mihailzharkovskiy.stepcounter.services.StepCounterNotification.Companion.NOTIF_ID
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener {
    @Inject
    lateinit var useCase: TickerUseCase

    @Inject
    lateinit var notifStep: StepCounterNotification

    @Inject
    lateinit var sensorManager: SensorManager

    /**Service**/
    override fun onCreate() {
        super.onCreate()
        startListenSensor()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListenSensor()
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {
                ACTION_START_SERVICE -> startForeground(NOTIF_ID, notifStep.createNotification())
                ACTION_POUSE_SERVICE -> stopSelf()
            }
        }
        return START_STICKY
    }

    /**SensorEventListener**/
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_STEP_DETECTOR -> step(StepCounterSensorEvent.YesStepSensor)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun step(event: StepCounterSensorEvent) {
        if (event is StepCounterSensorEvent.YesStepSensor) {
            val data = useCase.doStep()
            notifStep.updateNotification(data.steps, data.km, data.stepsPlane)
        }
    }

    private fun startListenSensor() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    private fun stopListenSensor() {
        sensorManager.unregisterListener(this)
    }

    companion object {
        private const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        private const val ACTION_POUSE_SERVICE = "ACTION_POUSE_SERVICE"

        fun startService(context: Context) {
            val intent = Intent(context, StepCounterService::class.java)
            intent.action = ACTION_START_SERVICE
            ContextCompat.startForegroundService(context, intent)
        }

        fun pouseService(context: Context) {
            val intent = Intent(context, StepCounterService::class.java)
            intent.action = ACTION_POUSE_SERVICE
            ContextCompat.startForegroundService(context, intent)
        }
    }
}







