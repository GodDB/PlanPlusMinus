package com.example.todoplusminus.controllers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanEditBinding
import com.example.todoplusminus.ui.CustomEditText
import com.example.todoplusminus.util.KeyboardDetector
import com.example.todoplusminus.vm.PlanEditVM

class PlanEditController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(vm : PlanEditVM){
        this.mVM = vm
    }

    private lateinit var binder: ControllerPlanEditBinding
    private lateinit var mKeyboardDetector: KeyboardDetector
    private var mVM : PlanEditVM? = null

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanEditBinding.inflate(inflater, container, false)
        binder.lifecycleOwner = this
        binder.vm = mVM
        return binder.root
    }

    override fun onViewBound(v: View) {
        mVM?.let {
            binder.colorSelectorView.setVM(it)
            binder.titleSelectorView.setVM(it)
        }
        configureUI()
        addEvent()
        onSubscribe()
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        mKeyboardDetector.start()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        mKeyboardDetector.stop()
    }

    private fun configureUI() {
        showKeypad()

    }

    private fun addEvent() {
        mKeyboardDetector = KeyboardDetector(binder.rootView)
        mKeyboardDetector.setOnKeyboardChangedListener(keyboardChangeListener)

        binder.itemTitle.setDelegate(noHideKeypadDelegate)
    }

    private fun onSubscribe(){
        mVM?.isEditEnd?.observe(this, Observer { isComplete ->
            if(isComplete){
                hideKeypad()
                popCurrentController()
            }
        })
    }

    private fun showKeypad() {
        val inputManager =
            binder.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    private fun hideKeypad() {
        val inputManager =
            binder.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    /**
     * 키보드 변경을 감지하기 위한 listener
     * */
    private val keyboardChangeListener = object : KeyboardDetector.OnKeyboardChangedListener {
        override fun onKeyboardChanged(visible: Boolean, height: Int) {
            if (visible) {
                binder.keyboardOverlayView.y = height.toFloat() + binder.keyboardOverlayView.height
                binder.keyboardOverlayView.visibility = View.VISIBLE

            } else {
                binder.keyboardOverlayView.visibility = View.GONE
            }
        }
    }

/*    *//**
     * colorSelectView로 부터 사용자가 선택한 color값을 전달받기 위한 listener
     * *//*
    private val colorSelectorListener = object : ColorSelectorView.Delegate {
        override fun onSelect(bgColor: Int) {
            mVM?.setBgColor(bgColor)
        }

        override fun onDone() {}
    }

    *//**
     * titleSelectView로 부터 사용자가 선택한 title값을 전달받기 위한 listener
     * *//*
    private val titleSelectorListener = object : TitleSelectorView.Delegate {
        override fun onSelect(title: String) {
            mVM?.setTitle(title)
        }
    }*/

    private val noHideKeypadDelegate = object : CustomEditText.Delegate {
        override fun onBack() {
            popCurrentController()
        }
    }


}