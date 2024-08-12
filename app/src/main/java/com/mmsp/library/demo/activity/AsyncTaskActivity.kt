package com.mmsp.library.demo.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mmsp.library.R
import com.mmsp.library.databinding.ActivityAsyncTaskBinding
import com.mmsp.library.uikit.BaseActivity
import com.mmsp.library.utils.SuperAsyncTask

class AsyncTaskActivity : BaseActivity<ActivityAsyncTaskBinding>() {

    private var mTask: SuperAsyncTask<String, Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun layoutId(): Int {
        return R.layout.activity_async_task
    }

    override fun initView() {
        initToolbar(mBinding.toolbarLayout.toolbar)
        mBinding.btnStart.setOnClickListener {
            if (mTask == null) {
                mTask = object : SuperAsyncTask<String, Long>(this) {
                    override fun doInBackground(vararg params: String): Long {
                        for (i in 0..100) {
                            publishProgress(i.toFloat())
                            Log.e("AsyncTask", "Progress: $i")
                            Thread.sleep(100)
                        }
                        return 100
                    }

                    override fun onProgressUpdate(progress: Float) {
                        mBinding.info.text = Editable.Factory.getInstance().newEditable("Progress: $progress%")
                    }

                    override fun onPostExecute(result: Long) {
                        mBinding.info.text = Editable.Factory.getInstance().newEditable("Finished")
                    }
                }
                mTask?.execute("")
            }
        }
        mBinding.btnCancel.setOnClickListener {
            mTask?.cancel()
            mTask = null
        }
    }

    override fun initData() {

    }
}