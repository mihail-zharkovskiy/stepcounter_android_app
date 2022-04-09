package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika

import android.graphics.Color
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButtonToggleGroup
import dagger.hilt.android.AndroidEntryPoint
import developer.mihailzharkovskiy.stepcounter.databinding.DialogStatistikaBinding
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika.model.DataForChart
import developer.mihailzharkovskiy.stepcounter.ui.util.BaseDialogFragment
import im.dacer.androidcharts.LineView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DialogStatistics : BaseDialogFragment<DialogStatistikaBinding>() {

    private val viewModel: DialogStatisticsViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): DialogStatistikaBinding {
        return DialogStatistikaBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toggleButton.addOnButtonCheckedListener(toggleGroupListener)

        viewModel.getDataForSpecificTime(7)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { uiState ->
                    when (uiState) {
                        is DialogStatisticsState.NoData -> {
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                        is DialogStatisticsState.YesData -> {
                            binding.tvNoData.visibility = View.GONE
                            binding.statisticsAllTime.renderPrams(uiState.data.sumarnieDanie)
                            binding.statistikaSrednemZaDen.renderPrams(uiState.data.danieVSrednemZaDen)
                            drawGrafika(uiState.data.dataForGrafik)
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        binding.toggleButton.removeOnButtonCheckedListener(toggleGroupListener)
        super.onDestroyView()
    }

    private fun drawGrafika(data: DataForChart) {
        binding.grafic.apply {
            setDrawDotLine(false)
            setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY)
            setColorArray(intArrayOf(Color.BLACK))
            setBottomTextList(data.listDate)
            setDataList(arrayListOf(data.listStep))
        }
    }

    private val toggleGroupListener =
        MaterialButtonToggleGroup.OnButtonCheckedListener { toggle, checkedId, isChecked ->
            toggle.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            if (isChecked) {
                val days = when (checkedId) {
                    binding.bt7day.id -> 7
                    binding.bt30day.id -> 30
                    else -> 90
                }
                viewModel.getDataForSpecificTime(days)
            }
        }

    companion object {
        private const val TAG = "DialogStatistics"
        fun show(manager: FragmentManager) {
            DialogStatistics().show(manager, TAG)
        }
    }
}
