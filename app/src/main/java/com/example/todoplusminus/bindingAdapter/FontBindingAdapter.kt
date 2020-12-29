package com.example.todoplusminus.bindingAdapter

import android.graphics.Typeface

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.todoplusminus.ui.customViews.PMCalendarView
import com.example.todoplusminus.ui.customViews.SubWatchView

@BindingAdapter("bind:font")
fun setFont(tv : TextView, font : Typeface?){
    if(font == null) return

    tv.typeface = font
}

@BindingAdapter("bind:font")
fun setFont(v : SubWatchView, font : Typeface?){
    if(font == null) return

    v.setFont(font)
}

@BindingAdapter("bind:font")
fun setFont(v : PMCalendarView, font: Typeface?){
    if(font == null) return

    v.setFont(font)
}

@BindingAdapter("bind:planSize")
fun setTextSize(v : TextView, size : Int){
    v.textSize = size.toFloat()
}