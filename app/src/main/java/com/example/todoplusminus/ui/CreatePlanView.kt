package com.example.todoplusminus.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.example.todoplusminus.databinding.UiCreatePlanViewBinding


class CreatePlanView : LinearLayout  {

    interface Delegate{
        fun onDone(title : String, bgColor : Int)
    }


    private var title : String = ""
    private var bgColor : Int = Color.DKGRAY

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

    fun setBgColor(){

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
                    this.title = v.text.toString()
                    mDelegate?.onDone(title, bgColor)
                    clear()
                    true
                }
            }
        }
    }

    private fun clear(){
        binder.btnEdit
        binder.btnEdit.visibility = View.VISIBLE
        binder.editableTitle.clearComposingText()
        binder.editableTitle.visibility = View.GONE
        keyboard.hideSoftInputFromWindow(binder.editableTitle.windowToken, 0)
    }

}