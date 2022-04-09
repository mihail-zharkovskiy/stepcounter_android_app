package developer.mihailzharkovskiy.stepcounter.common.resource_provider

import android.content.res.Resources

interface ResourceProvider {
    fun getResources(): Resources
    fun getString(stringId: Int): String
}