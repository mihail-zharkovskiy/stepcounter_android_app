package developer.mihailzharkovskiy.stepcounter.common.resource_provider

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceProviderImpl(
    private val context: Context,
) : ResourceProvider {

    override fun getResources(): Resources {
        return context.resources
    }

    override fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }
}