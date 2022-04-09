package developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter

import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepAllTimeDataEntity
import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepForNowDataEntity
import developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.model.MainActivityUiModel
import java.text.DecimalFormat
import java.util.*

data class TickerUseCaseModel(
    val date: Date,
    val steps: Int,
    val km: Double,
    val kkal: Double,
    val progress: Int,
    val stepsPlane: Int,
)

// т.к приложение маленькое,дабы не захламлять код, реализация мапперов через итрефейсы заменена
// на extension функции

fun TickerUseCaseModel.mapToUiModel(): MainActivityUiModel {
    return MainActivityUiModel(
        progress = this.progress,
        steps = this.steps.toString(),
        kkal = DecimalFormat("#0.00").format(this.kkal).toString(),
        km = DecimalFormat("#0.00").format(this.km).toString(),
        stepPlane = this.stepsPlane.toString()
    )
}

fun TickerUseCaseModel.mapToStepForNowDataEntity(): StepForNowDataEntity {
    return StepForNowDataEntity(
        steps = this.steps,
        km = this.km,
        progress = this.progress,
        kkal = this.kkal,
        stepPlane = this.stepsPlane,
    )
}

fun TickerUseCaseModel.mapToAllTimeDataEntity(): StepAllTimeDataEntity {
    return StepAllTimeDataEntity(
        steps = this.steps,
        km = this.km,
        progres = this.progress,
        kkal = this.kkal,
        stepPlane = this.stepsPlane
    )
}