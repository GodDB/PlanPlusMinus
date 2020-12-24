package com.example.todoplusminus.customViews

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.todoplusminus.databinding.UiSubwathviewBinding
import com.example.todoplusminus.util.DateHelper
import kotlinx.coroutines.*

class SubWatchView : LinearLayout {

    private var mJob: Job = Job()
    private lateinit var vb : UiSubwathviewBinding

    constructor(context: Context?) : super(context) {
        customInit()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        customInit()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        customInit()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        (vb.clockIv.background as AnimationDrawable).stop()
        mJob.cancel()
    }

    fun setFont(font : Typeface?){
        if(font == null) return

        vb.clockTime.typeface = font
    }

    fun start() {
        (vb.clockIv.background as AnimationDrawable).start()

        CoroutineScope(Dispatchers.Main + mJob).launch {
            while (true) {
                vb.clockTime.text = DateHelper.getRemainingTimeInDay()
                delay(500)
            }
        }
    }

    private fun customInit(){
        vb = UiSubwathviewBinding.inflate(LayoutInflater.from(context), this, true)
    }

}