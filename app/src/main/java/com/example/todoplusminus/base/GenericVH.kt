package com.example.todoplusminus.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class GenericVH(itemView : View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind()
}