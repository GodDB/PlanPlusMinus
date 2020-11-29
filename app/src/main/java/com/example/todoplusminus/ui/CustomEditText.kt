package com.example.todoplusminus.ui

import android.content.Context
import android.text.method.KeyListener
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText

/**
 * 키패드를 내리는 행위 (backpress)를 감지할 수 있는 EditText
 * */
class CustomEditText : androidx.appcompat.widget.AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    interface Delegate {
        fun onBack()
    }

    private var mDelegate: Delegate? = null


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            mDelegate?.onBack()
        }
        return super.onKeyPreIme(keyCode, event)
    }


    fun setDelegate(delegate: Delegate) {
        mDelegate = delegate
    }
}