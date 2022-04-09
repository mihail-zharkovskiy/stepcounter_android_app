package developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import developer.mihailzharkovskiy.stepcounter.domain.DomainDataState
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCase
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCaseModel
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika.model.DataForChart
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika.model.DataForTextView
import developer.mihailzharkovskiy.stepcounter.ui.screens.dialog_statistika.model.DialogStatisticsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class DialogStatisticsViewModel @Inject constructor(
    private val statisticsUseCase: StatisticsUseCase,
) : ViewModel() {

    private val decimalFormat = DecimalFormat("#0.00")

    private val _uiState = MutableStateFlow<DialogStatisticsState>(DialogStatisticsState.NoData)
    val uiState: StateFlow<DialogStatisticsState> get() = _uiState.asStateFlow()

    init {
        getDataForSpecificTime()
    }

    fun getDataForSpecificTime(howManyDays: Int = 7) = viewModelScope.launch {
        when (val danie = statisticsUseCase.getDataForSpecificTime(howManyDays)) {
            is DomainDataState.YesData -> {
                _uiState.value = DialogStatisticsState.YesData(prepareDataForUi(danie.data))
            }
            is DomainDataState.NoData -> {
                _uiState.value = DialogStatisticsState.NoData
            }
        }
    }

    private fun prepareDataForUi(danie: List<StatisticsUseCaseModel>): DialogStatisticsUiModel {
        var sumSteps = 0
        var sumKm = 0.0
        var sumKkal = 0.0
        val numberOfDay = danie.size
        danie.forEach {
            sumSteps += it.steps
            sumKm += it.meters
            sumKkal += it.kkal
        }
        val dataForChart = renderDataForChart(danie)
        val dataAllTime = renderAllTimeData(sumSteps, sumKm, sumKkal)
        val dataSummary = renderOnAveragePerDayData(sumSteps, sumKm, sumKkal, numberOfDay)

        return DialogStatisticsUiModel(dataForChart, dataSummary, dataAllTime)
    }

    private fun renderAllTimeData(
        sumSteps: Int,
        sumMeters: Double,
        sumKkal: Double,
    ): DataForTextView {
        return DataForTextView(
            steps = sumSteps.toString(),
            km = decimalFormat.format(sumMeters / 1000),
            kkal = decimalFormat.format(sumKkal)
        )
    }

    private fun renderOnAveragePerDayData(
        sumSteps: Int,
        sumMeters: Double,
        sumKkal: Double,
        numberOfDay: Int,
    ): DataForTextView {
        return DataForTextView(
            steps = (sumSteps / numberOfDay).toString(),
            km = decimalFormat.format((sumMeters / 1000) / numberOfDay),
            kkal = decimalFormat.format(sumKkal / numberOfDay)
        )
    }

    private fun renderDataForChart(data: List<StatisticsUseCaseModel>): DataForChart {
        val listDate = arrayListOf<String>()
        val listStep = arrayListOf<Int>()
        data.forEach { it ->
            listDate.add(it.date)
            listStep.add(it.steps)
        }
        return DataForChart(listDate, listStep)
    }
}