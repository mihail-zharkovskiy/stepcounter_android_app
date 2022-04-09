package developer.mihailzharkovskiy.stepcounter.domain


sealed class DomainDataState<T> {
    class NoData<T> : DomainDataState<T>()
    class YesData<T>(val data: T) : DomainDataState<T>()
}

//data class DomainDataState<T>(val status: Status, val data: T?) {
//    companion object {
//        fun <T> noData():DomainDataState<T> = DomainDataState(Status.NO_DATA,null)
//        fun <T> yesData(data: T):DomainDataState<T> =  DomainDataState(Status.YES_DATA,data)
//    }
//}
//
//enum class Status {
//    NO_DATA,
//    YES_DATA
//}


