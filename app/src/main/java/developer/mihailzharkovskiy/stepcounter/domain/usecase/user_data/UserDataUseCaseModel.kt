package developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data

import developer.mihailzharkovskiy.stepcounter.data.room.entity.UserDataEntity
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_settings.DialogSettingsUiModel

class UserDataUseCaseModel(
    val height: Int,
    val weight: Int,
    val stepPlane: Int,
)

// т.к приложение маленькое,дабы не захламлять код, реализация мапперов через итрефейсы заменена
// на extension функции

fun UserDataUseCaseModel.mapToUiModel(): DialogSettingsUiModel {
    return DialogSettingsUiModel(
        height = this.height.toString(),
        weight = this.weight.toString(),
        stepPlane = this.stepPlane.toString()
    )
}

fun UserDataUseCaseModel.mapToEntity(): UserDataEntity {
    return UserDataEntity(
        rost = this.height,
        ves = this.weight,
        normaShagov = this.stepPlane
    )
}