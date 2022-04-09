package developer.mihailzharkovskiy.stepcounter.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import developer.mihailzharkovskiy.stepcounter.common.resource_provider.ResourceProvider
import developer.mihailzharkovskiy.stepcounter.common.resource_provider.ResourceProviderImpl

@Module
@InstallIn(SingletonComponent::class)
class ResourceModul {

    @Provides
    fun bindResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }
}