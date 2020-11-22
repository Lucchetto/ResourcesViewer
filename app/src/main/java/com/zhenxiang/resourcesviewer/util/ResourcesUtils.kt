package com.zhenxiang.resourcesviewer.util

import android.content.Context
import android.content.res.Resources
import com.zhenxiang.resourcesviewer.Resource

class ResourcesUtils {
    companion object {
        val resTypes = arrayOf("dimen", "string", "bool", "integer", "color")
        var value : String? = null;
        fun getResourceValue(resId : Int, resType : String, context: Context) : String? {
            value = when (resType) {
                "dimen" -> context.resources.getDimensionPixelSize(resId).toString()
                "string" -> context.getString(resId)
                "bool" -> context.resources.getBoolean(resId).toString()
                "integer" -> context.resources.getInteger(resId).toString()
                "color" -> "#${Integer.toHexString(context.resources.getColor(resId))}"
                else -> null
            }
            return value
        }
        fun getAndroidResourceByName(resName: String, context: Context) : Resource? {
            for (resType in resTypes) {
                val resId = getResourceIdByName(resName, resType, "android", context)
                if (resId != 0) {
                    return Resource(resId, resType)
                }
            }
            return null
        }

        fun getAndroidResourceByName(resName: String, resType : String, context: Context) : Resource? {
            val resId = getResourceIdByName(resName, resType, "android", context)
            if (resId != 0) {
                return Resource(resId, resType)
            }
            return null
        }

        fun getResourceIdByName(resName: String, resType : String, packageName : String, context : Context) : Int {
            val resources = context.packageManager.getResourcesForApplication(packageName)
            return resources.getIdentifier(resName, resType, packageName)
        }
    }
}