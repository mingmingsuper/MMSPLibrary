package com.mmsp.library.utils

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat

class PermissionUtils {

    companion object {

        fun checkPermission(context: Context, permissions: Array<out String>): Boolean {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        fun requestPermission(activity: Activity, permissions: Array<String>, requestCode: Int) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
    }
}