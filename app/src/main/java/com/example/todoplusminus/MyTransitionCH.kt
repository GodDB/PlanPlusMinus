package com.example.todoplusminus

import android.transition.*
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler


class MyTransitionCH : TransitionChangeHandler {
    constructor() : super()

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