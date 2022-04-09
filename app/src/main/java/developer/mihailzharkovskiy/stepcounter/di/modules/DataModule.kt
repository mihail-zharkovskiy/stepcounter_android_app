package developer.mihailzharkovskiy.stepcounter.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import developer.mihailzharkovskiy.stepcounter.common.dispatchers_provider.DispatcherProvider
import developer.mihailzharkovskiy.stepcounter.data.repository.RepositoryImpl
import developer.mihailzharkovskiy.stepcounter.data.room.StepsDataBase
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoAllTimeData
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataForNow
import developer.mihailzharkovskiy.stepcounter.data.room.dao.DaoDataUser
import developer.mihailzharkovskiy.stepcounter.di.anatations.ReleaseDataBase
import developer.mihailzharkovskiy.stepcounter.domain.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

////  SHARED_PREFERENCES
//    @Provides
//    fun provideSheredPref(@ApplicationContext context: Context): SharedPreferences {
//        return context.getSharedPreferences(Constance.ForSharedPref.KEY_SHARED_PREF, Context.MODE_PRIVATE)
//    }

    //  REPOSITORY
    @Provides
    @Singleton
    fun provideRepository(
        daoAllTime: DaoAllTimeData,
        daoDataForNow: DaoDataForNow,
        daoUserData: DaoDataUser,
        dispatcherProvider: DispatcherProvider,
    ): Repository {
        return RepositoryImpl(daoDataForNow, daoAllTime, daoUserData, dispatcherProvider)
    }

    //  ROOM
    @ReleaseDataBase
    @Provides
    @Singleton
    fun provideStepsDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, StepsDataBase::class.java, StepsDataBase.dataBaseName)
            .build()

    @Provides
    @Singleton
    fun provideDaoDataForNow(@ReleaseDataBase dataBase: StepsDataBase) =
        dataBase.getDaoDataForNow()

    @Provides
    @Singleton
    fun provideDaoAllTimeData(@ReleaseDataBase dataBase: StepsDataBase) =
        dataBase.getDaoAllTimeData()

    @Provides
    @Singleton
    fun provideDaoUserData(@ReleaseDataBase dataBase: StepsDataBase) =
        dataBase.getDaoUserData()
}
