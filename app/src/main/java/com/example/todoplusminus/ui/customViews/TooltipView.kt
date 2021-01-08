package com.example.todoplusminus.ui.customViews

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.example.todoplusminus.R
import com.example.todoplusminus.databinding.UiTooltipViewBinding

/**
 * 하나의 텍스트뷰를 가진 tooltipView
 *
 * 사용자에게 간략한 메시지를 전달한다.
 * */
class TooltipView : FrameLayout {

    companion object {
        const val TOOLTIP_ARROW_LEFT: Int = 0
        const val TOOLTIP_ARROW_RIGHT: Int = 1
        const val TOOLTIP_ARROW_TOP: Int = 2
        const val TOOLTIP_ARROW_BOTTOM: Int = 3
    }

    private lateinit var binder: UiTooltipViewBinding

    constructor(context: Context) : super(context) {
        customInit(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customInit(context)
        setupAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        customInit(context)
        setupAttrs(context, attrs)
    }


    private fun customInit(context: Context) {
        binder = UiTooltipViewBinding.inflate(LayoutInflater.from(context), this, true)

        binder.tooltipAllowLeft.tag = TOOLTIP_ARROW_LEFT
        binder.tooltipAllowRight.tag = TOOLTIP_ARROW_RIGHT
        binder.tooltipAllowTop.tag = TOOLTIP_ARROW_TOP
        binder.tooltipAllowBottom.tag = TOOLTIP_ARROW_BOTTOM
    }



    fun setColor(colorId: Int) {

        val blendFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(colorId, BlendModeCompat.SRC_IN)

        binder.tooltipAllowLeft.colorFilter = blendFilter
        binder.tooltipAllowTop.colorFilter = blendFilter
        binder.tooltipAllowBottom.colorFilter = blendFilter
        binder.tooltipAllowRight.colorFilter = blendFilter
        binder.tooltipBody.setCardBackgroundColor(colorId)
    }

    fun setText(text: String) {
        binder.tooltipTv.text = text
    }

    fun setText(strId: Int) {
        binder.tooltipTv.text = context.getText(strId)
    }

    fun setTooltipArrowDirection(direction: Int) {
        showTooltipArrowBy(direction)
    }

    private fun showTooltipArrowBy(direction: Int) {
        val tooltipArrows = listOf(
            binder.tooltipAllowLeft,
            binder.tooltipAllowRight,
            binder.tooltipAllowTop,
            binder.tooltipAllowBottom
        )

        tooltipArrows.forEach {
            if (direction == it.tag) it.visibility = View.VISIBLE
            else it.visibility = View.GONE
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        if (visibility == View.VISIBLE) return startTooltipAnim()

        stopTooltipAnim()
    }


    private fun startTooltipAnim() {
        val moveTopProperty = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -20f)
        val moveDownProperty = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 20f)

        val animator =
            ObjectAnimator.ofPropertyValuesHolder(this, moveTopProperty, moveDownProperty)
                .apply {
                    duration = 500
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }
        this.tag = animator
        animator.start()
    }


    private fun stopTooltipAnim() {
        (this.tag as? ObjectAnimator)?.run {
            cancel()
            end()
        }
    }


    /** attribute set을 이용하여 뷰를 초기화한다.
     *
     * customInit()후에 호출해야한다... (viewBinding 초기화 때문에)
     * */
    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attrs, R.styleable.TooltipView)

        val direction = attrs.getInteger(R.styleable.TooltipView_arrowDirection, TOOLTIP_ARROW_TOP)
        val bgColor =  attrs.getColor(R.styleable.TooltipView_toolTipColor, Color.CYAN)
        val text = attrs.getString(R.styleable.TooltipView_text) ?: ""


        setText(text)
        setTooltipArrowDirection(direction)
        setColor(bgColor)
    }
}