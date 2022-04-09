package developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import developer.mihailzharkovskiy.stepcounter.domain.DomainDataState
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCase
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.mapToUiModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.TickerUseCase
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.mapToUiModel
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
    private val tickerUseCase: TickerUseCase,
    private val statistikaUseCase: StatisticsUseCase,
) : ViewModel(), LifecycleObserver {

    private val _uiState =
        MutableStateFlow<MainActivityUiState>(MainActivityUiState.NoStatisticsData)
    val uiState: StateFlow<MainActivityUiState> get() = _uiState.asStateFlow()

    private val _dataStepCounter = MutableStateFlow(MainActivityUiModel())
    val dataStepCounter: StateFlow<MainActivityUiModel> get() = _dataStepCounter.asStateFlow()

    init {
        checkUserData()
        getAllTimeData()
        observeStepCounterData()
    }

    fun saveDataForNow() = viewModelScope.launch { tickerUseCase.saveDataForNow() }

    fun saveDataForTheDay() = viewModelScope.launch { tickerUseCase.saveDataForTheDay() }

    fun clearDatabase() = viewModelScope.launch {
        statistikaUseCase.deleteAll()
        _uiState.value = MainActivityUiState.NoStatisticsData
    }

    private fun observeStepCounterData() = viewModelScope.launch {
        tickerUseCase.tickerData.collect {
            if (it != null) {
                _dataStepCounter.value = it.mapToUiModel()
            }
        }
    }

    private fun getAllTimeData() = viewModelScope.launch {
        statistikaUseCase.getAllStatistics().collect { state ->
            when (state) {
                is DomainDataState.NoData -> {
                    _uiState.value = MainActivityUiState.NoStatisticsData
                }
                is DomainDataState.YesData -> {
                    _uiState.value =
                        MainActivityUiState.YesStatisticsData(state.data.map { it.mapToUiModel() }
                            .reversed())
                }
            }
        }
    }

    private fun checkUserData() = viewModelScope.launch {
        userDataUseCase.getUserData().collect { data ->
            when (data) {
                is DomainDataState.NoData -> _uiState.value = MainActivityUiState.NoUserData
                is DomainDataState.YesData -> _uiState.value = MainActivityUiState.YesUserData
            }
        }
    }
}