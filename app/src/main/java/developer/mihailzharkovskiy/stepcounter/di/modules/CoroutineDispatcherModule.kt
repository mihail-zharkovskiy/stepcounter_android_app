package developer.mihailzharkovskiy.stepcounter.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import developer.mihailzharkovskiy.stepcounter.common.dispatchers_provider.DispatcherProvider
import developer.mihailzharkovskiy.stepcounter.common.dispatchers_provider.DispatcherProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineDispatcherModule {
    @Provides
    @Singleton
    fun provaidCoroutineDispatcher(): DispatcherProvider = DispatcherProviderImpl()
}