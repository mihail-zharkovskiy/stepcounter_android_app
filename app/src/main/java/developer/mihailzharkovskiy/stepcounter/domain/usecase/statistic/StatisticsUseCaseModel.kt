package developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic


data class StatisticsUseCaseModel(
    val date: String,
    val steps: Int,
    val kkal: Double,
    val meters: Double,
    val stepPlane: Int,
    val progress: Int,
)


