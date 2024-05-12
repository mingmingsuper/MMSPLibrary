package com.mmsp.library.utils

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView的工具类
 *
 * @author: mmsp
 * @date: 2024/05/10
 */
class RecyclerViewUtils {

    companion object {
        private const val TAG = "RecyclerViewUtils"

        /**
         * 添加RecyclerView的点击事件
         *
         * @param context 上下文
         * @param recyclerView RecyclerView
         * @param listener RecyclerView的点击事件回调
         */
        fun addRecyclerListener(context: Context, recyclerView: RecyclerView?, listener: RecyclerListener?) {
            recyclerView?.apply {
                addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    var gestureDetector: GestureDetectorCompat = GestureDetectorCompat(
                        context,
                        object : GestureDetector.SimpleOnGestureListener() {
                            override fun onSingleTapUp(e: MotionEvent): Boolean {
                                val child = recyclerView.findChildViewUnder(e.x, e.y)
                                if (child != null) {
                                    val position = recyclerView.getChildAdapterPosition(child)
                                    if (position != -1) {
                                        Log.e(TAG, "onClick position: $position")
                                        listener?.onClick(position)
                                    }
                                }
                                return super.onSingleTapUp(e)
                            }

                            override fun onLongPress(e: MotionEvent) {
                                val child = recyclerView.findChildViewUnder(e.x, e.y)
                                if (child != null) {
                                    val position = recyclerView.getChildAdapterPosition(child)
                                    if (position != -1) {
                                        Log.e(TAG, "longPress position: $position")
                                        listener?.onLongPress(position)
                                    }
                                }
                                super.onLongPress(e)
                            }
                        })

                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        gestureDetector.onTouchEvent(e)
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    }

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                    }

                })
            }
        }
    }
}

interface RecyclerListener {
    fun onClick(position: Int)
    fun onLongPress(position: Int) {
        // 默认实现
    }
}