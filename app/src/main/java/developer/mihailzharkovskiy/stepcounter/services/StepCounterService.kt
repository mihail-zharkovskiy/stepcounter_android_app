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
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.CounterUseCase
import developer.mihailzharkovskiy.stepcounter.services.StepCounterNotification.Companion.NOTIF_ID
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener {
    @Inject
    lateinit var useCase: CounterUseCase

    @Inject
    lateinit var notifStep: StepCounterNotification

    @Inject
    lateinit var sensorManager: SensorManager

    private fun step() {
        val data = useCase.doStep()
        notifStep.updateNotification(data.steps, data.km, data.stepsPlane)
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

    // SERVICE
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
                ACTION_PAUSE_SERVICE -> stopSelf()
            }
        }
        return START_STICKY
    }

    // SENSOR EVENT LISTENER
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_STEP_DETECTOR -> step()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    companion object {
        private const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        private const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"

        /**для api 29+ вызывай только если permission.ACTIVITY_RECOGNITION == PERMISSION_GRANTED
         * для api < 29 вызывай без проверок.
         * TODO(#1 @RequiresApi(Build.VERSION_CODES.Q) @RequiresPermission(Manifest.permission.ACTIVITY_RECOGNITION))
         * **/

        fun startService(context: Context) {
            val intent = Intent(context, StepCounterService::class.java)
            intent.action = ACTION_START_SERVICE
            ContextCompat.startForegroundService(context, intent)
        }

        //НА БУДУЩЕЕ
        fun pauseService(context: Context) {
            val intent = Intent(context, StepCounterService::class.java)
            intent.action = ACTION_PAUSE_SERVICE
            ContextCompat.startForegroundService(context, intent)
        }
    }
}







