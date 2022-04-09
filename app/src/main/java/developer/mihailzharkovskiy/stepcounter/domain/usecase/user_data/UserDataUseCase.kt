package developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data

import androidx.core.text.isDigitsOnly
import developer.mihailzharkovskiy.stepcounter.domain.DomainDataState
import developer.mihailzharkovskiy.stepcounter.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataUseCase @Inject constructor(
    private val repository: Repository,
) {

    fun getUserData(): Flow<DomainDataState<UserDataUseCaseModel>> {
        return repository.getUserData().map { data ->
            if (data != null) DomainDataState.YesData(data)
            else DomainDataState.NoData()
        }
    }

    suspend fun updateUserData(
        weight: String,
        growth: String,
        stepPlane: String,
    ): UserDataUpdateState {
        return when {
            weight.isEmpty() || growth.isEmpty() || stepPlane.isEmpty() -> {
                UserDataUpdateState.NoData
            }
            weight.isDigitsOnly() && growth.isDigitsOnly() && stepPlane.isDigitsOnly() -> {
                saveNewData(weight, growth, stepPlane)
            }
            else -> UserDataUpdateState.Invalidate(InvalidateStatus.WRITE_NOT_A_NUMBER)
        }
    }

    private suspend fun saveNewData(weight: String, growth: String, stepPlane: String) =
        if (weight.toInt() > 0 && growth.toInt() > 0 && stepPlane.toInt() > 0) {
            repository.saveUserData(UserDataUseCaseModel(growth.toInt(),
                weight.toInt(),
                stepPlane.toInt()))
            UserDataUpdateState.Success
        } else UserDataUpdateState.Invalidate(InvalidateStatus.WRITE_NIL)

}


