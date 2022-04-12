package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_settings

import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCaseModel

fun UserDataUseCaseModel.mapToiModel(): DialogSettingsUiModel {
    return DialogSettingsUiModel(
        height = this.height.toString(),
        weight = this.weight.toString(),
        stepPlane = this.stepPlane.toString()
    )
}