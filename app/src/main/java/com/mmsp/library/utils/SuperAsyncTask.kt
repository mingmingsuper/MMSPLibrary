package com.mmsp.library.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executors

abstract class SuperAsyncTask<T,U>(private val context: Context) {
    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    private var isCanceled = false

    fun cancel() {
        executor.shutdownNow()
    }

    fun isCanceled(): Boolean {
        return isCanceled
    }

    fun execute(vararg params: T) {
        executor.execute {
            try {
                val result = doInBackground(*params)
                runOnUIThread {
                    onPostExecute(result)
                }
            } catch (e: InterruptedException) {
                Log.e("SuperAsyncTask", "Interrupted Exception")
            }

        }
    }

    fun publishProgress(progress: Float) {
        runOnUIThread {
            onProgressUpdate(progress)
        }
    }

    private fun runOnUIThread(runnable: Runnable) {
        if (Thread.currentThread() != context.mainLooper.thread) {
            handler.post(runnable)
        } else {
            runnable.run()
        }
    }

    protected abstract fun doInBackground(vararg params: T): U

    protected abstract fun onProgressUpdate(progress: Float)

    protected abstract fun onPostExecute(result: U)

}