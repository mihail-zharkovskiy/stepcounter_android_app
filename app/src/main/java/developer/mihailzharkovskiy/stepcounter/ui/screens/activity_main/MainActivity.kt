package developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.robinhood.ticker.TickerUtils
import dagger.hilt.android.AndroidEntryPoint
import developer.mihailzharkovskiy.stepcounter.R
import developer.mihailzharkovskiy.stepcounter.databinding.ActivityMainBinding
import developer.mihailzharkovskiy.stepcounter.services.StepCounterService
import developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.adapter.AdapterSteps
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_settings.DialogSettings
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika.DialogStatistics
import developer.mihailzharkovskiy.stepcounter.ui.util.checkingStepSensorPermission
import developer.mihailzharkovskiy.stepcounter.ui.util.goToAppSetting
import developer.mihailzharkovskiy.stepcounter.ui.util.requestStepSensorPermission
import developer.mihailzharkovskiy.stepcounter.ui.util.toast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private val stepAdapter: AdapterSteps by lazy { AdapterSteps() }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        startStepCounter()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeDataStepCounter() }
                launch { observeUiState() }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveDataForNow()
    }

    private fun setupView() {
        with(binding) {
            rvStatistikaShagov.apply {
                adapter = stepAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            tvTikerMeters.setCharacterLists(TickerUtils.provideNumberList())
            tvTikerKalorii.setCharacterLists(TickerUtils.provideNumberList())

            btSave.setOnClickListener { viewModel.saveDataForTheDay() }
            btDelete.setOnClickListener { showSnackBarDelete(it) }
            btSetting.setOnClickListener { DialogSettings.show(supportFragmentManager) }
            btStatistics.setOnClickListener { DialogStatistics.show(supportFragmentManager) }
        }
    }

    private suspend fun observeDataStepCounter() {
        viewModel.dataStepCounter.collect { data ->
            binding.tickerAndProgrss.renderParams(data.progress, data.steps, data.stepPlane)
            binding.tvTikerMeters.text = data.km
            binding.tvTikerKalorii.text = data.kkal
        }
    }

    private suspend fun observeUiState() {
        viewModel.uiState.collect { state ->
            when (state) {
                is MainActivityUiState.NoStatisticsData -> {
                    binding.rvStatistikaShagov.visibility = View.INVISIBLE
                    binding.tvNoStatisticsData.visibility = View.VISIBLE
                }
                is MainActivityUiState.YesStatisticsData -> {
                    binding.rvStatistikaShagov.visibility = View.VISIBLE
                    binding.tvNoStatisticsData.visibility = View.INVISIBLE
                    stepAdapter.differ.submitList(state.data)
                }
                is MainActivityUiState.NoUserData -> {
                    binding.tvNoUserData.visibility = View.VISIBLE
                    DialogSettings.show(supportFragmentManager)
                }
                is MainActivityUiState.YesUserData -> {
                    binding.tvNoUserData.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STEP_SENSOR_PERMISSION -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)) {
                        goToAppSetting()
                    }
                } else toast(getString(R.string.dper_restart))
            }
        }
    }

    private fun startStepCounter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkingStepSensorPermission()) {
                StepCounterService.startService(this)
            } else requestStepSensorPermission(REQUEST_CODE_STEP_SENSOR_PERMISSION)
        } else StepCounterService.startService(this)
    }

    private fun showSnackBarDelete(view: View) {
        Snackbar.make(view, getString(R.string.snack_bar_delete_header), Snackbar.LENGTH_LONG)
            .apply {
                anchorView = view
                setTextColor(getColor(R.color.snow))
                setActionTextColor(getColor(R.color.red))
                setBackgroundTint(getColor(R.color.heavy_clouds))
                setAction(getString(R.string.snack_bar_delete_action)) { viewModel.clearDatabase() }
            }.show()
    }

    companion object {
        private const val REQUEST_CODE_STEP_SENSOR_PERMISSION = 1
    }
}