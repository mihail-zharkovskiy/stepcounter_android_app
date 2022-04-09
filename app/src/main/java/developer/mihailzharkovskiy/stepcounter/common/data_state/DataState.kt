package developer.mihailzharkovskiy.stepcounter.common.data_state


data class DataState<T>(val status: Status, val message: String?, val data: T?) {
    companion object {
        fun <T> success(data: T): DataState<T> {
            return DataState(Status.SUCCESS, null, data)
        }

        fun <T> loading(): DataState<T> {
            return DataState(Status.LOADING, null, null)
        }

        fun <T> empty(): DataState<T> {
            return DataState(Status.EMPTY, null, null)
        }

        fun <T> error(message: String): DataState<T> {
            return DataState(Status.ERROR, message, null)
        }
    }
}