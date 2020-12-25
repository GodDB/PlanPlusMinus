package com.example.todoplusminus.util.ChangeHandlers

import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler


class MyTransitionCH : TransitionChangeHandler {

    constructor()
    constructor(removesOnPushView : Boolean){
        this.removesOnPushView = removesOnPushView
    }

    private var removesOnPushView : Boolean = true

    override fun removesFromViewOnPush(): Boolean {
        return removesOnPushView
    }

    override fun getTransition(
        container: ViewGroup,
        from: View?,
        to: View?,
        isPush: Boolean
    ): Transition {
        val changeTransform = ChangeTransform()
        changeTransform.reparentWithOverlay = true
        val transitionSet = TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(ChangeClipBounds())
            addTransition(changeTransform)
            addTransition(ChangeImageTransform())

            duration = 300
        }

        transitionSet.ordering = TransitionSet.ORDERING_TOGETHER
        return transitionSet
    }




}