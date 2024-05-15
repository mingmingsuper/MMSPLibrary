package com.mmsp.library.demo

import android.app.Activity
import android.content.Context
import com.mmsp.library.R
import com.mmsp.library.demo.activity.DocumentAccessActivity
import com.mmsp.library.demo.activity.TakePhotoActivity

class DemoDataSource {
    companion object {

        fun createDemoList(context: Context): List<MainMenuItem> {
            val list = ArrayList<MainMenuItem>()
            list.add(
                MainMenuItem(
                    DemoMenuType.TakePhoto,
                    context.getString(R.string.activity_title_get_photo),
                    TakePhotoActivity::class.java
                )
            )
            list.add(
                MainMenuItem(
                    DemoMenuType.DocumentAccess,
                    context.getString(R.string.activity_title_document_access),
                    DocumentAccessActivity::class.java
                )
            )
            return list
        }
    }
}

class MainMenuItem(val type: DemoMenuType, val title: String, val activity: Class<out Activity>)

enum class DemoMenuType {
    TakePhoto,
    DocumentAccess
}
