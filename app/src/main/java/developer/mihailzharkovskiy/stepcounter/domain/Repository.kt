package developer.mihailzharkovskiy.stepcounter.domain

import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepForNowDataEntity
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.TickerUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCaseModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllTimeData(): Flow<List<StatisticsUseCaseModel>>

    fun getUserData(): Flow<UserDataUseCaseModel?>

    suspend fun saveUserData(userData: UserDataUseCaseModel)

    suspend fun deleteAllData(): List<StatisticsUseCaseModel>

    suspend fun getDataForSpecificTime(howManyDays: Int): List<StatisticsUseCaseModel>

    suspend fun restoreToDayData(): StepForNowDataEntity?

    /**сохранение данных в onPouse**/
    suspend fun saveDataForNow(data: TickerUseCaseModel?)

    /**сохранение данных в статистику**/
    suspend fun saveDataForTheDay(data: TickerUseCaseModel?)

}