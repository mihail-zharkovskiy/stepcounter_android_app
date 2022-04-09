package developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data

sealed class UserDataUseCaseUpdateState {
    object NoDataUseCaseUpdate : UserDataUseCaseUpdateState()
    object Success : UserDataUseCaseUpdateState()
    object InvalidDataUseCaseUpdate : UserDataUseCaseUpdateState()
}
