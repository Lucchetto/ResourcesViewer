package com.zhenxiang.resourcesviewer.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import com.zhenxiang.resourcesviewer.Resource


class ResourcesUtils {
    companion object {
        val resTypes = arrayOf("dimen", "string", "bool", "integer", "color", "array")

        fun getResourceValue(resource: Resource, context: Context) : String? {
            var value : String?;
            val resources = context.packageManager.getResourcesForApplication(resource.packageName)
            value = when (resource.resType) {
                "dimen" -> {
                    val outValue = TypedValue()
                    resources.getValue(resource.resId, outValue, true)
                    val pixelDimen = resources.getDimensionPixelSize(resource.resId)
                    val rawDimen = TypedValue.complexToFloat(outValue.data)
                    "${pixelDimen}px ${rawDimen}${getDimenUnitString(getComplexUnit(outValue.data))}"
                }
                "string" -> resources.getString(resource.resId)
                "bool" -> resources.getBoolean(resource.resId).toString()
                "integer" -> resources.getInteger(resource.resId).toString()
                "color" -> "#${Integer.toHexString(resources.getColor(resource.resId))}"
                "array" -> System.lineSeparator() + getArrayResourceValues(
                    resource,
                    context
                ).joinToString(
                    System.lineSeparator()
                )
                else -> null
            }
            return value
        }

        fun getComplexUnit(data: Int): Int {
            return TypedValue.COMPLEX_UNIT_MASK and (data shr TypedValue.COMPLEX_UNIT_SHIFT)
        }

        fun getDimenUnitString(unit: Int) : String {
            return when(unit) {
                TypedValue.COMPLEX_UNIT_DIP -> "dp"
                TypedValue.COMPLEX_UNIT_IN -> "in"
                TypedValue.COMPLEX_UNIT_MM -> "mm"
                TypedValue.COMPLEX_UNIT_PX -> "px"
                TypedValue.COMPLEX_UNIT_SP -> "sp"
                TypedValue.COMPLEX_UNIT_PT -> "pt"
                else -> ""
            }
        }

        fun getArrayResourceValues(resource: Resource, context: Context) : Array<String> {
            var values : Array<String> = emptyArray();
            var isIntArray : Boolean;
            val resources = context.packageManager.getResourcesForApplication(resource.packageName)
            if (resources.getStringArray(resource.resId).isNotEmpty()) {
                isIntArray = resources.getStringArray(resource.resId)[0] == null
                if (isIntArray) {
                    values = convertIntToStringArray(resources.getIntArray(resource.resId))
                } else {
                    values = resources.getStringArray(resource.resId)
                }
            }
            return values
        }

        fun convertIntToStringArray(intArray: IntArray) : Array<String> {
            if (intArray.isEmpty()) {
                return emptyArray()
            }
            val stringArray : Array<String> = Array(intArray.size) { "" }
            for (i in intArray.indices) {
                stringArray[i] = intArray[i].toString()
            }
            return stringArray
        }

        fun getResourceByName(resName: String, packageName: String, context: Context) : Resource? {
            for (resType in resTypes) {
                val resId = getResourceIdByName(resName, resType, packageName, context)
                if (resId != 0) {
                    return Resource(resId, resType, packageName)
                }
            }
            return null
        }

        fun getResourceByName(
            resName: String,
            resType: String,
            packageName: String,
            context: Context
        ) : Resource? {
            val resId = getResourceIdByName(resName, resType, packageName, context)
            if (resId != 0) {
                return Resource(resId, resType, packageName)
            }
            return null
        }

        fun getResourceIdByName(
            resName: String,
            resType: String,
            packageName: String,
            context: Context
        ) : Int {
            val resources = context.packageManager.getResourcesForApplication(packageName)
            return resources.getIdentifier(resName, resType, packageName)
        }
    }
}