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
        height: String,
        stepPlane: String,
    ): UserDataUpdateState {
        return when {
            weight.isEmpty() || height.isEmpty() || stepPlane.isEmpty() -> {
                UserDataUpdateState.NoData
            }
            weight.isDigitsOnly() && height.isDigitsOnly() && stepPlane.isDigitsOnly() -> {
                saveNewData(weight.toInt(), height.toInt(), stepPlane.toInt())
            }
            else -> UserDataUpdateState.Invalidate(InvalidateStatus.WRITE_NOT_A_NUMBER)
        }
    }

    private suspend fun saveNewData(weight: Int, height: Int, stepPlane: Int): UserDataUpdateState {
        return if (weight > 0 && height > 0 && stepPlane > 0) {
            repository.saveUserData(UserDataUseCaseModel(height, weight, stepPlane))
            UserDataUpdateState.Success
        } else UserDataUpdateState.Invalidate(InvalidateStatus.WRITE_NIL)
    }
}


