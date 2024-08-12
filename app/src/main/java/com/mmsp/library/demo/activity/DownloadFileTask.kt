package com.mmsp.library.demo.activity

import android.content.Context
import com.mmsp.library.utils.SuperAsyncTask

class DownloadFileTask(context: Context) : SuperAsyncTask<String, Long>(context){

    override fun doInBackground(vararg params: String): Long {
        for (i in 0..100) {
            Thread.sleep(1000)
            publishProgress(i.toFloat())
        }
        return 0
    }

    override fun onProgressUpdate(progress: Float) {

    }

    override fun onPostExecute(result: Long) {
    }

}