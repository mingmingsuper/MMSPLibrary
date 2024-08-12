package com.mmsp.library

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmsp.library.databinding.ActivityMainBinding
import com.mmsp.library.demo.DemoDataSource
import com.mmsp.library.demo.DemoMenuType
import com.mmsp.library.demo.DemoRoute
import com.mmsp.library.demo.MainMenuItem
import com.mmsp.library.uikit.BaseActivity
import com.mmsp.library.uikit.recyclerview.RecyclerListener
import com.mmsp.library.uikit.recyclerview.RecyclerViewUtils
import com.mmsp.library.uikit.recyclerview.addListener

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var list: List<MainMenuItem>

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
        mBinding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mBinding.recyclerView.addListener(this, object :
            RecyclerListener {
            override fun onClick(position: Int) {
                val item = list[position]
                when (item.type) {
                    DemoMenuType.TakePhoto -> DemoRoute.forwardDemo(this@MainActivity, item.activity)
                    DemoMenuType.DocumentAccess -> DemoRoute.forwardDemo(this@MainActivity, item.activity)
                    DemoMenuType.AsyncTask -> DemoRoute.forwardDemo(this@MainActivity, item.activity)
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