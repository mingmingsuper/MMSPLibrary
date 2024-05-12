package com.mmsp.library.uikit.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<U : ViewDataBinding, T : BaseViewHolder<U>> : RecyclerView.Adapter<T>()