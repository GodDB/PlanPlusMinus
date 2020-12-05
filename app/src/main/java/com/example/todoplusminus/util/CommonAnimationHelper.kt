package com.example.todoplusminus.util

import android.animation.*
import android.util.Log
import android.view.View
import android.view.animation.Animation

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


    fun startFadeAnimation(v : View, isShowing : Boolean){
        if(isShowing)
            v.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(object: AnimatorListenerAdapter(){
                    override fun onAnimationStart(animation: Animator?) {
                        v.visibility = View.VISIBLE
                    }
                })

        else
            v.animate()
                .alpha(0f)
                .setDuration(0)
                .setListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        v.visibility = View.GONE
                    }
                })

    }


    fun stop(v : View){
        (v.tag as? ObjectAnimator)?.run {
            cancel()
            end()
        }
    }

}