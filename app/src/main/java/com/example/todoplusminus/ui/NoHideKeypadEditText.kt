package com.example.todoplusminus.ui

import android.content.Context
import android.text.method.KeyListener
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText

/**
 * backpress를 눌러도 keypad가 사라지지 않는 editText
 *
 * 대신 backpress가 안되므로 delegate fuction을 통해 원하는 작업을 수행해야 한다.
 * */
class NoHideKeypadEditText : EditText {

    interface Delegate{
        fun onBack()
    }

    private var mDelegate : Delegate? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if(event?.keyCode == KeyEvent.KEYCODE_BACK){
            mDelegate?.onBack()
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }


    fun setDelegate(delegate : Delegate){
        mDelegate = delegate
    }
}