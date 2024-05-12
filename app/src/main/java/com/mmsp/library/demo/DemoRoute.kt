package com.mmsp.library.demo

import android.app.Activity
import android.content.Context
import android.content.Intent

class DemoRoute {

    companion object {
        fun forwardDemo(context: Context, activity: Class<out Activity>) {
            val intent = Intent(context, activity)
            context.startActivity(intent)
        }
    }
}