package developer.mihailzharkovskiy.stepcounter.data.repository

import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepAllTimeDataEntity
import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepForNowDataEntity
import developer.mihailzharkovskiy.stepcounter.data.room.entity.UserDataEntity
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.TickerUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCaseModel

// т.к приложение маленькое,дабы не захламлять код, реализация мапперов через итрефейсы заменена
// на extension функции

fun StepForNowDataEntity.mapToDomainModel(): TickerUseCaseModel {
    return TickerUseCaseModel(
        steps = this.steps,
        km = this.km,
        progress = this.progress,
        kkal = this.kkal,
        stepsPlane = this.stepPlane,
        date = this.date
    )
}

fun UserDataEntity.mapToDomainModel(): UserDataUseCaseModel {
    return UserDataUseCaseModel(
        height = this.height,
        weight = this.weight,
        stepPlane = this.stepPlane
    )
}

fun StepAllTimeDataEntity.mapToDomainModel(): StatisticsUseCaseModel {
    return StatisticsUseCaseModel(
        date = this.date,
        steps = this.steps,
        kkal = this.kkal,
        meters = this.km,
        stepPlane = this.stepPlane,
        progress = this.progress,
    )
}

fun UserDataUseCaseModel.mapToEntity(): UserDataEntity {
    return UserDataEntity(
        height = this.height,
        weight = this.weight,
        stepPlane = this.stepPlane
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
        progress = this.progress,
        kkal = this.kkal,
        stepPlane = this.stepsPlane
    )
}