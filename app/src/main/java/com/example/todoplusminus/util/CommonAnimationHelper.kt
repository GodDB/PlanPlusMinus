package com.example.todoplusminus.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

object CommonAnimationHelper {

    private val rotateLeftProperty = PropertyValuesHolder.ofFloat(View.ROTATION, 15f)
    private val rotateRightProperty = PropertyValuesHolder.ofFloat(View.ROTATION, -15f)

    fun startVibrateAnimation(v: View) {
        stop(v)

        val animator = ObjectAnimator.ofPropertyValuesHolder(v, rotateLeftProperty, rotateRightProperty).apply {
            duration = 100
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        v.tag = animator
        animator.start()
    }


    fun stop(v : View){
        (v.tag as? ObjectAnimator)?.run {
            cancel()
            end()
        }
    }

}