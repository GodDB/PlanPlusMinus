package com.example.todoplusminus.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.todoplusminus.util.TimeHelper
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

    fun startAnimation(){
        CoroutineScope(Dispatchers.Main + mJob).launch {
            while(true){
                this@MainWatchView.text = TimeHelper.getCurDateTime()
                delay(10000)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mJob.cancel()
    }
}