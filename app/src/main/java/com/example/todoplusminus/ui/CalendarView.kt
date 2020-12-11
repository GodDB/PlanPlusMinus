package com.example.todoplusminus.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.todoplusminus.databinding.UiCalendarViewBinding

class CalendarView : LinearLayout {

    private lateinit var binder : UiCalendarViewBinding

    constructor(context: Context?) : super(context){
        customInit(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        customInit(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        customInit(context)
    }



    private fun customInit(context: Context?){
        binder = UiCalendarViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
}