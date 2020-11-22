package com.zhenxiang.resourcesviewer.util

import android.content.Context

class PackageUtils {
    companion object {
        fun isPackageInstalled(context: Context, target: String): Boolean {
            return context.packageManager.getInstalledApplications(0).find { info -> info.packageName == target } != null
        }
    }
}