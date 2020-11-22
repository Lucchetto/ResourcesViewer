package com.zhenxiang.resourcesviewer.util

import android.content.Context
import android.content.res.Resources
import com.zhenxiang.resourcesviewer.Resource

class ResourcesUtils {
    companion object {
        val resTypes = arrayOf("dimen", "string", "bool", "integer", "color")
        var value : String? = null;
        fun getResourceValue(resource : Resource, context: Context) : String? {
            val resources = context.packageManager.getResourcesForApplication(resource.packageName)
            value = when (resource.resType) {
                "dimen" -> "${resources.getDimensionPixelSize(resource.resId).toString()} px"
                "string" -> resources.getString(resource.resId)
                "bool" -> resources.getBoolean(resource.resId).toString()
                "integer" -> resources.getInteger(resource.resId).toString()
                "color" -> "#${Integer.toHexString(resources.getColor(resource.resId))}"
                else -> null
            }
            return value
        }
        fun getResourceByName(resName: String, packageName : String, context: Context) : Resource? {
            for (resType in resTypes) {
                val resId = getResourceIdByName(resName, resType, packageName, context)
                if (resId != 0) {
                    return Resource(resId, resType, packageName)
                }
            }
            return null
        }

        fun getResourceByName(resName: String, resType : String, packageName : String,  context: Context) : Resource? {
            val resId = getResourceIdByName(resName, resType, packageName, context)
            if (resId != 0) {
                return Resource(resId, resType, packageName)
            }
            return null
        }

        fun getResourceIdByName(resName: String, resType : String, packageName : String, context : Context) : Int {
            val resources = context.packageManager.getResourcesForApplication(packageName)
            return resources.getIdentifier(resName, resType, packageName)
        }
    }
}