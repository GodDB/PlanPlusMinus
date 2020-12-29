package com.example.todoplusminus.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.todoplusminus.util.DateHelper
import kotlinx.coroutines.*

class MainWatchView : AppCompatTextView {

    private var mJob: Job = Job()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if(visibility == View.GONE) return stop()
        if(visibility == View.VISIBLE) return start()
    }

    fun start(){
        stop()
        CoroutineScope(Dispatchers.Main + mJob).launch {
            while(true){
                this@MainWatchView.text = DateHelper.getCurDateTime()
                delay(10000)
            }
        }
    }

    fun stop(){
        mJob.cancel()
        mJob = Job()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mJob.cancel()
    }
}