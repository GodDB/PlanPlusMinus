package com.example.todoplusminus.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.vm.SettingData


abstract class GenericVH(itemView : View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data : Pair<Int, SettingData>)
}