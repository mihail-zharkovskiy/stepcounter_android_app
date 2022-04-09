package developer.mihailzharkovskiy.stepcounter.data.repository

import developer.mihailzharkovskiy.stepcounter.common.dispatchers_provider.DispatcherProvider
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoAllTimeData
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataForNow
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataUser
import developer.mihailzharkovskiy.stepcounter.data.room.entity.StepForNowDataEntity
import developer.mihailzharkovskiy.stepcounter.data.room.entity.mapToDomainModel
import developer.mihailzharkovskiy.stepcounter.data.room.entity.plus
import developer.mihailzharkovskiy.stepcounter.domain.Repository
import developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic.StatisticsUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.TickerUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.mapToAllTimeDataEntity
import developer.mihailzharkovskiy.stepcounter.domain.usecase.step_counter.mapToStepForNowDataEntity
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.UserDataUseCaseModel
import developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data.mapToEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val daoDataForNow: DaoDataForNow,
    private val daoAllTimeData: DaoAllTimeData,
    private val daoDataUser: DaoDataUser,
    private val dispatcher: DispatcherProvider,
) : Repository {

    override fun getAllTimeData(): Flow<List<StatisticsUseCaseModel>> {
        return daoAllTimeData.getAllTimeData()
            .map { entity -> entity.map { it.mapToDomainModel() } }
            .flowOn(dispatcher.io)
    }

    override fun getUserData(): Flow<UserDataUseCaseModel?> {
        return daoDataUser.getUserData()
            .map { entity -> entity?.mapToDomainModel() ?: null }
            .flowOn(dispatcher.io)
    }

    override suspend fun getDataForSpecificTime(howManyDays: Int): List<StatisticsUseCaseModel> {
        return withContext(dispatcher.io) {
            daoAllTimeData.getDataForSpecificTime(howManyDays).map { entity ->
                entity.mapToDomainModel()
            }.reversed()
        }
    }

    override suspend fun deleteAllData(): List<StatisticsUseCaseModel> {
        return withContext(dispatcher.io) {
            daoAllTimeData.deletAll()
            daoDataForNow.deleteAll()
            emptyList()
        }
    }

    override suspend fun restoreToDayData(): StepForNowDataEntity? {
        return withContext(dispatcher.io) {
            if (daoDataForNow.proverkaEstLiDanieVtablice() == 0) null
            else daoDataForNow.getDanieNaDaniyMoment()
        }
    }

    override suspend fun saveUserData(userData: UserDataUseCaseModel) {
        withContext(dispatcher.io) {
            daoDataUser.deleteAll()
            daoDataUser.insertUserData(userData.mapToEntity())
        }
    }

    override suspend fun saveDataForNow(data: TickerUseCaseModel?) {
        withContext(dispatcher.io) {
            if (data != null) {
                daoDataForNow.deleteAll()
                daoDataForNow.saveDanieNaDaniyMoment(data.mapToStepForNowDataEntity())
            }
        }
    }

    override suspend fun saveDataForTheDay(data: TickerUseCaseModel?) {
        withContext(dispatcher.io) {
            data?.let {
                val newData = data.mapToAllTimeDataEntity()
                when (val oldData = daoAllTimeData.getLastSaveData()) {
                    null -> daoAllTimeData.saveData(newData)
                    else -> daoAllTimeData.saveData(oldData.plus(newData))
                }
                daoDataForNow.deleteAll()
            }
        }
    }
}