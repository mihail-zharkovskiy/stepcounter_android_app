package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import developer.mihailzharkovskiy.stepcounter.R
import developer.mihailzharkovskiy.stepcounter.common.resource_provider.ResourceProvider
import developer.mihailzharkovskiy.stepcounter.domain.DomainDataState
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCase
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCaseUpdateState
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.mapToUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogSettingsViewModel @Inject constructor(
    private val userDataUseCase: UserDataUseCase,
    private val resource: ResourceProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DialogSettingsState>(DialogSettingsState.NoData(
        resource.getString(R.string.dsettings_no_data)))
    val uiState: StateFlow<DialogSettingsState> get() = _uiState.asStateFlow()

    fun getUserData() = viewModelScope.launch {
        userDataUseCase.getUserData().collect { dataState ->
            when (dataState) {
                is DomainDataState.YesData -> {
                    _uiState.value = DialogSettingsState.YesData(dataState.data.mapToUiModel())
                }
                is DomainDataState.NoData -> {
                    _uiState.value =
                        DialogSettingsState.NoData(resource.getString(R.string.dsettings_no_data))
                }
            }
        }
    }

    /**
     * дабы уменьшить количество проверок
     * функция работает только с edit text в который можно вводить только числа
     * **/
    fun saveUserData(weight: String, growth: String, stepPlane: String) = viewModelScope.launch {
        when (userDataUseCase.updateUserData(growth = growth,
            weight = weight,
            stepPlane = stepPlane)) {
            is UserDataUseCaseUpdateState.InvalidDataUseCaseUpdate -> {
                _uiState.value =
                    DialogSettingsState.InvalidData(resource.getString(R.string.dsettings_error_write_null))
            }
            is UserDataUseCaseUpdateState.NoDataUseCaseUpdate -> {
                _uiState.value =
                    DialogSettingsState.NoData(resource.getString(R.string.dsettings_no_data))
            }
            is UserDataUseCaseUpdateState.Success -> _uiState.value = DialogSettingsState.Close
        }
    }
}
