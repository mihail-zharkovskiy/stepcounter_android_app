package developer.mihailzharkovskiy.stepcounter.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import developer.mihailzharkovskiy.stepcounter.ui.util.resource_provider.ResourceProvider
import developer.mihailzharkovskiy.stepcounter.ui.util.resource_provider.ResourceProviderImpl

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {

    @Provides
    fun bindResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }
}