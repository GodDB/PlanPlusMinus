package com.example.todoplusminus.controllers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanEditBinding
import com.example.todoplusminus.ui.ColorSelectorView
import com.example.todoplusminus.ui.NoHideKeypadEditText
import com.example.todoplusminus.ui.TitleSelectorView
import com.example.todoplusminus.util.ColorManager
import com.example.todoplusminus.util.KeyboardDetector

class PlanEditController : VBControllerBase {

    interface Delegate {
        fun onComplete(index: Int, bgColor: Int, title: String)
        fun onCancel()
    }

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(index: Int, bgColor: Int, title: String) {
        this.mIndex = index
        this.mBgColor = bgColor
        this.mTitle = title
    }


    private lateinit var binder: ControllerPlanEditBinding
    private lateinit var mKeyboardDetector: KeyboardDetector
    private var mDelegate: Delegate? = null
    private var mIndex = -1
    private var mBgColor = ColorManager.getRandomColor()
    private var mTitle = ""


    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanEditBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        addEvent()
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        mKeyboardDetector.start()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        mKeyboardDetector.stop()
    }

    fun setDelegate(delegate: Delegate) {
        mDelegate = delegate
    }

    private fun configureUI() {
        setTitle(mTitle)
        setBgColor(mBgColor)
        binder.rootView.transitionName = mIndex.toString()

        showKeypad()
    }

    private fun addEvent() {
        mKeyboardDetector = KeyboardDetector(binder.rootView)
        mKeyboardDetector.setOnKeyboardChangedListener(keyboardChangeListener)

        binder.colorSelectorView.setDelegate(colorSelectorListener)
        binder.titleSelectorView.setDelegate(titleSelectorListener)
        binder.itemTitle.setDelegate(noHideKeypadDelegate)
    }

    private fun setBgColor(bgColor: Int) {
        mBgColor = bgColor
        binder.rootView.setCardBackgroundColor(bgColor)
    }

    private fun setTitle(title: String) {
        mTitle = title
        binder.itemTitle.text = null
        binder.itemTitle.setText(title)
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

    /**
     * colorSelectView로 부터 사용자가 선택한 color값을 전달받기 위한 listener
     * */
    private val colorSelectorListener = object : ColorSelectorView.Delegate {
        override fun onSelect(bgColor: Int) {
            setBgColor(bgColor)
        }

        override fun onDone() {}
    }

    /**
     * titleSelectView로 부터 사용자가 선택한 title값을 전달받기 위한 listener
     * */
    private val titleSelectorListener = object : TitleSelectorView.Delegate {
        override fun onSelect(title: String) {
            setTitle(title)
        }
    }

    private val noHideKeypadDelegate = object : NoHideKeypadEditText.Delegate {
        override fun onBack() {
            hideKeypad()
            mDelegate?.onCancel()
        }
    }


}