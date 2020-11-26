package com.example.todoplusminus.util

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

/**
 * 키보드가 올라왔는지 체크하기 위한 디텍터
 *
 *  정확히 키보드만 탐지할 수 없다... 하지만 보이는 부분만을 탐지 할 수 있다.
 *  보이는 부분 = 전체 화면 크기 - 보이지 않는 부분(statusBar + softNavigationBar + keyboard)
 *
 * targetView를 전달받아 targetView가 키보드의 일정 높이가 생성된다면 감지해서
 * callback을 전달해준다.
 * */
class KeyboardDetector(private val mTargetView : View?) : ViewTreeObserver.OnGlobalLayoutListener {

    interface OnKeyboardChangedListener{
        fun onKeyboardChanged(visible : Boolean, height : Int)
    }

    companion object{
        //키보드 높이 추정값
        private const val SOFT_INPUT_MIN_HEIGHT = 300;
    }


    private val mVisibleRect = Rect()
    private var mRectRight = 0
    private var mVisibleHeight = 0

    private var mIsVisibleSoftKeyboard = false
    private var mKeyboardHeight = 0

    // detect pause
    private var mDetectPaused = false

    //listener
    private var mListener : OnKeyboardChangedListener? = null

    fun start(){
        addOnGlobalLayoutListener(mTargetView, this)
        mDetectPaused = false
    }

    fun stop(){
        removeOnGlobalLayoutListener(mTargetView, this)
        mDetectPaused = true
    }

    fun setOnKeyboardChangedListener(listener : OnKeyboardChangedListener){
        this.mListener = listener
    }

    private fun addOnGlobalLayoutListener(v : View?, onGlobalLayoutListener : ViewTreeObserver.OnGlobalLayoutListener){
        v?.viewTreeObserver?.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    private fun removeOnGlobalLayoutListener(v : View?, onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener){
        v?.viewTreeObserver?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onGlobalLayout() {
        if(mTargetView == null) return

        mTargetView.getWindowVisibleDisplayFrame(mVisibleRect)

        var isChangeOrientation = false

        if(mRectRight == 0)
            mRectRight = mVisibleRect.right

        else{
            if(mRectRight != mVisibleRect.right){
                //화면이 회전된 것임
                isChangeOrientation = true
                mRectRight = mVisibleRect.right
            }else
                isChangeOrientation = false
        }

        val visibleHeight = mVisibleRect.height()

        if(!isChangeOrientation && this.mVisibleHeight != visibleHeight){
            this.mVisibleHeight = visibleHeight
            val statusBarHeight = mVisibleRect.top
            val navigationHeight = mTargetView.resources.getDimensionPixelSize(mTargetView.resources.getIdentifier("navigation_bar_height", "dimen", "android"))

            val softInputHeight = mTargetView.rootView.height - visibleHeight + navigationHeight + statusBarHeight

            val softInputVisible = softInputHeight > SOFT_INPUT_MIN_HEIGHT

            if(this.mIsVisibleSoftKeyboard != softInputVisible || this.mKeyboardHeight != softInputHeight){
                this.mIsVisibleSoftKeyboard = softInputVisible
                this.mKeyboardHeight = softInputHeight

                if(!mDetectPaused)
                    mListener?.onKeyboardChanged(softInputVisible, softInputHeight)
            }
        }
    }


}

