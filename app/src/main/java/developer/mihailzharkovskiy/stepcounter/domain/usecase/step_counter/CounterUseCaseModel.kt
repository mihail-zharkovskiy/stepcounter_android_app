package developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter

import java.util.*

data class TickerUseCaseModel(
    val date: Date,
    val steps: Int,
    val km: Double,
    val kkal: Double,
    val progress: Int,
    val stepsPlane: Int,
)

