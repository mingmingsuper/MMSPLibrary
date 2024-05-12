package com.mmsp.library.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException

class ActivityUtils {
    companion object {
        fun startActivity(context: Context, cls: Class<*>) {
            val intent = Intent(context, cls)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun startApplication(context: Context, packageName: String) {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            } catch (ex: NameNotFoundException) {
                ex.printStackTrace()
            }

            if (packageInfo != null) {
                return
            }

            val resolveInfoIntent = Intent(Intent.ACTION_MAIN, null)
            resolveInfoIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            resolveInfoIntent.setPackage(packageName)

            val resolveInfoList = context.packageManager.queryIntentActivities(resolveInfoIntent, 0)
            if (resolveInfoList.size > 0) {
                val resolveInfo = resolveInfoList[0]
                val activityInfo = resolveInfo.activityInfo
                if (activityInfo != null) {
                    val className = activityInfo.name
                    val pName = activityInfo.packageName
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                    val cn = ComponentName(pName, className)
                    intent.component = cn
                    context.startActivity(intent)
                }
            }
        }
    }
}