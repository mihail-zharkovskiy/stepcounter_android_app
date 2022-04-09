package developer.mihailzharkovskiy.stepcounter.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import developer.mihailzharkovskiy.stepcounter.di.anatations.DispatcherDefault
import developer.mihailzharkovskiy.stepcounter.di.anatations.DispatcherIo
import developer.mihailzharkovskiy.stepcounter.di.anatations.DispatcherMain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope =
        CoroutineScope(Dispatchers.Main)

    @DispatcherMain
    @Provides
    @Singleton
    fun provideDispatcherMain(): CoroutineDispatcher = Dispatchers.Main

    @DispatcherIo
    @Provides
    @Singleton
    fun provideDispatcherIo(): CoroutineDispatcher = Dispatchers.IO

    @DispatcherDefault
    @Provides
    @Singleton
    fun provideDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

}