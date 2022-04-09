package developer.mihailzharkovskiy.stepcounter.data.repository

import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoAllTimeData
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataForNow
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataUser
import developer.mihailzharkovskiy.stepcounter.di.anatations.DispatcherIo
import developer.mihailzharkovskiy.stepcounter.domain.Repository
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.TickerUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val daoDataForNow: DaoDataForNow,
    private val daoAllTimeData: DaoAllTimeData,
    private val daoDataUser: DaoDataUser,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher,
) : Repository {

    override fun getAllTimeData(): Flow<List<StatisticsUseCaseModel>> {
        return daoAllTimeData.getAllTimeData()
            .map { entity -> entity.map { it.mapToDomainModel() } }
            .flowOn(dispatcherIo)
    }

    override fun getUserData(): Flow<UserDataUseCaseModel?> {
        return daoDataUser.getUserData()
            .map { entity -> entity?.mapToDomainModel() }
            .flowOn(dispatcherIo)
    }

    override suspend fun getDataForSpecificTime(howManyDays: Int): List<StatisticsUseCaseModel> {
        return withContext(dispatcherIo) {
            daoAllTimeData.getDataForSpecificTime(howManyDays)
                .map { entity -> entity.mapToDomainModel() }
                .reversed()
        }
    }

    override suspend fun deleteAllData(): List<StatisticsUseCaseModel> {
        return withContext(dispatcherIo) {
            daoAllTimeData.deleteAll()
            daoDataForNow.deleteAll()
            emptyList()
        }
    }


    override suspend fun restoreToDayData(): TickerUseCaseModel? {
        return withContext(dispatcherIo) {
            daoDataForNow.getDataForNow()?.mapToDomainModel()
        }
    }

    override suspend fun saveUserData(userData: UserDataUseCaseModel) {
        withContext(dispatcherIo) {
            daoDataUser.deleteAll()
            daoDataUser.insertUserData(userData.mapToEntity())
        }
    }

    override suspend fun saveDataForNow(data: TickerUseCaseModel?) {
        withContext(dispatcherIo) {
            if (data != null) {
                daoDataForNow.deleteAll()
                daoDataForNow.saveDataForNow(data.mapToStepForNowDataEntity())
            }
        }
    }

    override suspend fun saveDataForTheDay(data: TickerUseCaseModel?) {
        withContext(dispatcherIo) {
            data?.let {
                val newData = data.mapToAllTimeDataEntity()
                when (val oldData = daoAllTimeData.getLastSaveData()) {
                    null -> daoAllTimeData.saveData(newData)
                    else -> daoAllTimeData.saveData(oldData.plusData(newData))
                }
                daoDataForNow.deleteAll()
            }
        }
    }
}