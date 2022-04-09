package developer.mihailzharkovskiy.stepcounter.domain.usecase.statistic

import developer.mihailzharkovskiy.stepcounter.domain.DomainDataState
import developer.mihailzharkovskiy.stepcounter.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatisticsUseCase @Inject constructor(
    private val repository: Repository,
) {

    fun getAllStatistics(): Flow<DomainDataState<List<StatisticsUseCaseModel>>> {
        return repository.getAllTimeData().map { data ->
            if (data.isNotEmpty()) DomainDataState.YesData(data)
            else DomainDataState.NoData()
        }
    }

    suspend fun getDataForSpecificTime(howManyDays: Int = 7): DomainDataState<List<StatisticsUseCaseModel>> {
        val data = repository.getDataForSpecificTime(howManyDays)
        return if (data.isNotEmpty()) DomainDataState.YesData(data)
        else DomainDataState.NoData()
    }

    suspend fun deleteAll(): DomainDataState<StatisticsUseCaseModel> {
        repository.deleteAllData()
        return DomainDataState.NoData()
    }
}



