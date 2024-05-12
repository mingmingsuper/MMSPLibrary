package com.mmsp.library

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import com.mmsp.library.databinding.MainActivityItemLayoutBinding
import com.mmsp.library.demo.MainMenuItem
import com.mmsp.library.uikit.recyclerview.BaseAdapter
import com.mmsp.library.uikit.recyclerview.BaseViewHolder

class MainActivityAdapter(private val context: Context, private val list: List<MainMenuItem>) :
    BaseAdapter<MainActivityItemLayoutBinding, ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MainActivityItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = list[position].title
    }

    override fun getItemCount(): Int {
        return list.size
    }


}

class ViewHolder(binding: MainActivityItemLayoutBinding) :
    BaseViewHolder<MainActivityItemLayoutBinding>(binding)