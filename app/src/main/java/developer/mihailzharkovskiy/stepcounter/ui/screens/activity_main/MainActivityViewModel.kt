package developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import developer.mihailzharkovskiy.stepcounter.domain.DomainDataState
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCase
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.CounterUseCase
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCase
import developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.model.MainActivityUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userDataUseCase: UserDataUseCase,
    private val tickerUseCase: CounterUseCase,
    private val statisticsUseCase: StatisticsUseCase,
) : ViewModel(), LifecycleObserver {

    private val _uiStatUiState =
        MutableStateFlow<MainActivityUiState>(MainActivityUiState.NoStatisticsData)
    val uiStatUiState: StateFlow<MainActivityUiState> get() = _uiStatUiState.asStateFlow()

    private val _stepCounterUiState = MutableStateFlow(MainActivityUiModel())
    val stepCounterUiState: StateFlow<MainActivityUiModel> get() = _stepCounterUiState.asStateFlow()

    init {
        checkUserData()
        getAllTimeData()
        observeStepCounterData()
    }

    fun saveDataForNow() = viewModelScope.launch { tickerUseCase.saveDataForNow() }

    fun saveDataForTheDay() = viewModelScope.launch { tickerUseCase.saveDataForTheDay() }

    fun clearDatabase() = viewModelScope.launch {
        statisticsUseCase.deleteAll()
        _uiStatUiState.value = MainActivityUiState.NoStatisticsData
    }

    private fun observeStepCounterData() = viewModelScope.launch {
        tickerUseCase.tickerData.collect {
            if (it != null) _stepCounterUiState.value = it.mapToUiModel()
        }
    }

    private fun getAllTimeData() = viewModelScope.launch {
        statisticsUseCase.getAllStatistics().collect { state ->
            when (state) {
                is DomainDataState.NoData -> {
                    _uiStatUiState.value = MainActivityUiState.NoStatisticsData
                }
                is DomainDataState.YesData -> {
                    _uiStatUiState.value = MainActivityUiState.YesStatisticsData(
                        state.data.map { it.mapToUiModel() }.reversed()
                    )
                }
            }
        }
    }

    private fun checkUserData() = viewModelScope.launch {
        userDataUseCase.getUserData().collect { data ->
            when (data) {
                is DomainDataState.NoData -> _uiStatUiState.value = MainActivityUiState.NoUserData
                is DomainDataState.YesData -> _uiStatUiState.value = MainActivityUiState.YesUserData
            }
        }
    }
}