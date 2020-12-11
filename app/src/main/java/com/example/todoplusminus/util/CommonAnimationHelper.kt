package com.example.todoplusminus.util

import android.animation.*
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation

object CommonAnimationHelper {

    private val rotateLeftProperty = PropertyValuesHolder.ofFloat(View.ROTATION, 15f)
    private val rotateRightProperty = PropertyValuesHolder.ofFloat(View.ROTATION, -15f)

    fun startVibrateAnimation(v: View) {
        stop(v)

        val animator =
            ObjectAnimator.ofPropertyValuesHolder(v, rotateLeftProperty, rotateRightProperty)
                .apply {
                    duration = 100
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }

        v.tag = animator
        animator.start()
    }


    fun startFadeInAnimation(v: View) {
        v.animate()
            .alpha(1f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    v.visibility = View.VISIBLE
                }
            })
    }

    fun startFadeOutAnimation(v : View){
        v.animate()
            .alpha(0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    v.visibility = View.GONE
                }
            })
    }

    fun startYMoveDownAnimation(v: View, y: Int) {
        ValueAnimator.ofInt(y).apply {
            duration = 300
            addUpdateListener {
                val animValue = it.animatedValue as Int
                val marginParam = (v.layoutParams as ViewGroup.MarginLayoutParams)
                marginParam.topMargin = animValue
                v.layoutParams = marginParam
            }
            start()
        }
    }

    fun startYMoveUpAnimation(v: View, y: Int) {
        ValueAnimator.ofInt(y).apply {
            duration = 300
            addUpdateListener {
                val animValue = it.animatedValue as Int
                val marginParam = (v.layoutParams as ViewGroup.MarginLayoutParams)
                marginParam.bottomMargin = animValue
                v.layoutParams = marginParam
            }
            start()
        }
    }

    fun stop(v: View) {
        (v.tag as? ObjectAnimator)?.run {
            cancel()
            end()
        }
    }

}