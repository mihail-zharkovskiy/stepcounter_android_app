package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistics

import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistics.model.DialogStatisticsUiModel

sealed class DialogStatisticsState {
    object NoData : DialogStatisticsState()
    class YesData(val data: DialogStatisticsUiModel) : DialogStatisticsState()
}