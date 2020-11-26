package com.example.todoplusminus.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.example.todoplusminus.databinding.UiCreatePlanViewBinding


class CreatePlanView : LinearLayout  {

    interface Delegate{
        fun onClick()
        fun onDone(title : String, bgColor : Int)
    }


    private var title : String = ""
    private var bgColor : Int = Color.LTGRAY

    private var mDelegate : Delegate? = null
    private lateinit var binder : UiCreatePlanViewBinding
    private lateinit var keyboard : InputMethodManager

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

    fun setDelegate(delegate: Delegate){
        this.mDelegate = delegate
    }

    fun setBgColor(bgColor : Int){
        this.bgColor = bgColor
        binder.rootView.setCardBackgroundColor(bgColor)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        mDelegate?.onClick()
        return false
    }

    private fun customInit(context: Context?){
        binder = UiCreatePlanViewBinding.inflate(LayoutInflater.from(context), this, true)
        keyboard = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        binder.btnEdit.setOnClickListener {
            it.visibility = View.GONE
            binder.editableTitle.visibility = View.VISIBLE
            binder.editableTitle.requestFocus()
            keyboard.showSoftInput(binder.editableTitle, 0)
        }

        binder.editableTitle.imeOptions = EditorInfo.IME_ACTION_DONE  //키보드 커스텀 (완료로 변경)
        binder.editableTitle.setOnEditorActionListener { v, actionId, _ ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE -> {
                    this.title = v.text.toString()
                    mDelegate?.onDone(title, bgColor)
                    clear()
                    true
                }
                else -> {
                    if(checkIsEmpty()) clear()
                    else{
                        this.title = v.text.toString()
                        mDelegate?.onDone(title, bgColor)
                        clear()
                    }
                    true
                }
            }
        }
    }

    private fun clear(){
        binder.btnEdit
        binder.btnEdit.visibility = View.VISIBLE
        binder.editableTitle.visibility = View.GONE
        binder.editableTitle.text = null
        resetBgColor()

        keyboard.hideSoftInputFromWindow(binder.editableTitle.windowToken, 0)
    }

    private fun checkIsEmpty() : Boolean = binder.editableTitle.text == null || binder.editableTitle.text.toString() == ""

    private fun resetBgColor(){
        this.bgColor = Color.LTGRAY
        binder.rootView.setCardBackgroundColor(bgColor)
    }



}