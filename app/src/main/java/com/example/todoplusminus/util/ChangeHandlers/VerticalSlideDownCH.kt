package com.example.todoplusminus.util.ChangeHandlers

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import java.util.*

class VerticalSlideDownCH : AnimatorChangeHandler{

    constructor() : super()
    constructor(removesFromViewOnPush: Boolean) : super(removesFromViewOnPush)
    constructor(duration : Long) : super(duration)
    constructor(duration : Long, removesFromViewOnPush: Boolean) : super(duration, removesFromViewOnPush)


    override fun getAnimator(
        container: ViewGroup,
        from: View?,
        to: View?,
        isPush: Boolean,
        toAddedToContainer: Boolean
    ): Animator {
        val animator = AnimatorSet()
        val viewAnimators: MutableList<Animator> =
            ArrayList()
        if (isPush && to != null) {
            viewAnimators.add(
                ObjectAnimator.ofFloat(
                    to,
                    View.TRANSLATION_Y,
                    - to.height.toFloat(),
                    0f
                )
            )
        } else if (!isPush && from != null) {
            viewAnimators.add(
                ObjectAnimator.ofFloat(
                    from,
                    View.TRANSLATION_Y,
                    0f
                    - from.height.toFloat()
                )
            )
        }
        animator.playTogether(viewAnimators)
        return animator
    }

    override fun resetFromView(from: View) {}

    override fun copy(): ControllerChangeHandler {
        return VerticalSlideDownCH(animationDuration, removesFromViewOnPush())
    }

}