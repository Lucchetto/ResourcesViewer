package com.zhenxiang.resourcesviewer.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class ReflectionUtils {
    public static Context getPackageContext(Context context, String packageName) {
        try {
            return context.getApplicationContext().createPackageContext(packageName,
                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static void getAllResourcesId(Context context, String packageName) {
        ClassLoader classLoader = context.getClassLoader();
        try {
            Class<?> idClass = classLoader.loadClass(packageName + ".R$id");
            Log.d("aaaaaaaaaaaaaa", "" + idClass.getFields().length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
