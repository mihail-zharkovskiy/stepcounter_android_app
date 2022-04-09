package developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter

import developer.mihailzharkovskiy.stepcounter.common.dispatchers_provider.DispatcherProvider
import developer.mihailzharkovskiy.stepcounter.domain.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TickerUseCase @Inject constructor(
    private val repositoriy: Repository,
    private val dispatcher: DispatcherProvider,
) {
    private var shagi = 0
    private var kkal = 0.0
    private var km = 0.0
    private var progress = 0

    private var rost = 0
    private var ves = 0
    private var stepsPlane = 0
    private var razmerShaga =
        (rost / 400.0) + 0.37 //непонятно? просто погугл формулу для расчета размера шага

    private var date = Date()

    private val _tickerData: MutableStateFlow<TickerUseCaseModel?> = MutableStateFlow(null)
    val tickerData: StateFlow<TickerUseCaseModel?> = _tickerData.asStateFlow()

    init {
        CoroutineScope(dispatcher.io).launch {
            restoreCounterData()
            getUserData()
        }
    }

    fun doStep(): TickerUseCaseModel {
        shagi++
        km += razmerShaga
        kkal = (shagi * razmerShaga) / 1000 * 0.5 * ves
        progress = (shagi * 100) / stepsPlane
        _tickerData.value = TickerUseCaseModel(date, shagi, km, kkal, progress, stepsPlane)
        return TickerUseCaseModel(date, shagi, km, kkal, progress, stepsPlane)
    }


    private suspend fun restoreCounterData() {
        repositoriy.restoreToDayData()?.let {
            shagi = it.steps
            kkal = it.kkal
            km = it.km
            progress = it.progress
            date = it.date
        }
    }

    private suspend fun getUserData() {
        repositoriy.getUserData().let { flow ->
            flow.collect { data ->
                if (data != null) {
                    rost = data.height
                    ves = data.weight
                    stepsPlane = data.stepPlane
                    razmerShaga = (rost / 400.0) + 0.37
                    progress = (shagi * 100) / stepsPlane
                    _tickerData.value =
                        TickerUseCaseModel(date, shagi, km, kkal, progress, stepsPlane)
                }
                //над этим else надо думать
                //else throw (Exception("НЕПОЛУЧИЛ ДАННЫЕ О ПОЛЬЗОВАТЕЛЕ В ТИКЕРЕ"))
            }
        }
    }

    suspend fun saveDataForNow() {
        repositoriy.saveDataForNow(tickerData.value)
    }

    suspend fun saveDataForTheDay() {
        repositoriy.saveDataForTheDay(tickerData.value)
        obnulitSchentchik()
    }

    private fun obnulitSchentchik() {
        shagi = 0
        km = 0.0
        kkal = 0.0
        progress = 0
        _tickerData.value = TickerUseCaseModel(date, shagi, km, kkal, progress, stepsPlane)
    }
}

