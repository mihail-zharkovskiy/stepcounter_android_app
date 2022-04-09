package developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic

import developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.model.AdapterStepsUiModel
import java.text.DecimalFormat

data class StatisticsUseCaseModel(
    val date: String,
    val steps: Int,
    val kkal: Double,
    val metrs: Double,
    val stepPlane: Int,
    val progress: Int,
)

// т.к приложение маленькое,дабы не захламлять код, реализация мапперов через итрефейсы заменена
// на extension функции

fun StatisticsUseCaseModel.mapToUiModel(): AdapterStepsUiModel {
    return AdapterStepsUiModel(
        date = this.date,
        progress = this.progress,
        steps = this.steps.toString(),
        km = DecimalFormat("#0.00").format(this.metrs / 1000).toString(),
        kkal = DecimalFormat("#0.0").format(this.kkal),
        stepPlane = this.stepPlane.toString()
    )
}
