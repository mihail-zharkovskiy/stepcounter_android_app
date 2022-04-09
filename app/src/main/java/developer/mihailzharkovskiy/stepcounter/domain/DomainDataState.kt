package developer.mihailzharkovskiy.stepcounter.domain


sealed class DomainDataState<T> {
    class NoData<T> : DomainDataState<T>()
    class YesData<T>(val data: T) : DomainDataState<T>()
}



