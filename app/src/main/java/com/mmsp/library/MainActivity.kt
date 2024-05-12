package com.mmsp.library

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmsp.library.uikit.BaseActivity
import com.mmsp.library.databinding.ActivityMainBinding
import com.mmsp.library.demo.DemoDataSource
import com.mmsp.library.demo.DemoMenuType
import com.mmsp.library.demo.DemoRoute
import com.mmsp.library.demo.MainMenuItem
import com.mmsp.library.utils.RecyclerListener
import com.mmsp.library.utils.RecyclerViewUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var list:List<MainMenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        initToolbar(mBinding.toolbarLayout.toolbar)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        RecyclerViewUtils.addRecyclerListener(this, mBinding.recyclerView, object : RecyclerListener {
            override fun onClick(position: Int) {
                val item = list[position]
                when (item.type) {
                    DemoMenuType.TakePhoto -> {
                        DemoRoute.forwardDemo(this@MainActivity, item.activity)
                    }
                }
            }
        })
    }

    override fun initData() {
        list = DemoDataSource.createDemoList(this)
        val adapter = MainActivityAdapter(this, list)
        mBinding.recyclerView.adapter = adapter
    }
}