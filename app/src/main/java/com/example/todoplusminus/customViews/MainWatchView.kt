package com.example.todoplusminus.customViews

import android.content.Context
import android.util.AttributeSet
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

    fun start(){
        CoroutineScope(Dispatchers.Main + mJob).launch {
            while(true){
                this@MainWatchView.text = DateHelper.getCurDateTime()
                delay(10000)
            }
        }
    }

    fun stop(){
        mJob.cancel()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mJob.cancel()
    }
}