package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_settings

sealed class DialogSettingsState {
    class NoData(val message: String) : DialogSettingsState()
    class YesData(val data: DialogSettingsUiModel) : DialogSettingsState()
    class InvalidData(val message: String) : DialogSettingsState()
    object Close : DialogSettingsState()
}