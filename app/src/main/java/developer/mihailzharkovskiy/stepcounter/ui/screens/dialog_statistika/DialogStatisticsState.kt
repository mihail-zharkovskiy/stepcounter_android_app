package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika

import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika.model.DialogStatistikaUiModel

sealed class DialogStatisticsState {
    object NoData : DialogStatisticsState()
    class YesData(val data: DialogStatistikaUiModel) : DialogStatisticsState()
}