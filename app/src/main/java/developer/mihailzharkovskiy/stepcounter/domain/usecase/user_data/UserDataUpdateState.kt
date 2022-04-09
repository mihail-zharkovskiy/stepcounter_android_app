package developer.mihailzharkovskiy.stepcounter.domain.usecase.user_data

sealed class UserDataUpdateState {
    object NoData : UserDataUpdateState()
    object Success : UserDataUpdateState()
    class Invalidate(val status: InvalidateStatus) : UserDataUpdateState()
}

enum class InvalidateStatus {
    WRITE_NIL,
    WRITE_NOT_A_NUMBER
}
