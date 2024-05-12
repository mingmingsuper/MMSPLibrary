package com.mmsp.library.uikit

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var mBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, layoutId())
        setStatusBarColor()
        initView()
        initData()
    }

    abstract fun layoutId(): Int

    abstract fun initView()

    abstract fun initData()

    private fun setStatusBarColor() {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    fun initToolbar(toolbar: Toolbar?) {

        toolbar?.apply {

            setSupportActionBar(this)

            supportActionBar?.apply {
                if (Build.VERSION.SDK_INT >= 21) {
                    elevation = 0f
                }
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return false
    }
}