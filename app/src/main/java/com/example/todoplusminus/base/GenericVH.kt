package com.example.todoplusminus.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.ui.setting.ValueData


abstract class GenericVH(itemView : View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data : Triple<Int, Int, ValueData>)
}