package com.example.todoplusminus.util

import android.content.Context

object DpConverter {

    private var mDensity : Float = 0f

    fun setUp(context : Context){
        mDensity = context.resources.displayMetrics.density
    }

    fun dpToPx(dp : Float) : Float{
        return dp * mDensity
    }

    fun pxToDp(px : Float) : Float{
        return px / mDensity
    }
}