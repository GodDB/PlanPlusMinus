package com.example.todoplusminus.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class AlarmView : FrameLayout {
    constructor(context: Context) : super(context){
        customInit(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        customInit(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        customInit(context)
    }


    private fun customInit(context: Context){

    }
}