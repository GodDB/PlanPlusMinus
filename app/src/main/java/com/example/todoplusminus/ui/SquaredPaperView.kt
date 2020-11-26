package com.example.todoplusminus.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.todoplusminus.util.DpConverter

class SquaredPaperView : View{
    private val mPaint = Paint()
    private val interval = DpConverter.dpToPx(10f)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var focusX = 0f
        var focusY = 0f

        mPaint.color = Color.LTGRAY
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 1f

        while(focusY <= height){
            while(focusX <= width){
                canvas?.drawLine(focusX, focusY, focusX+interval, focusY, mPaint) //top
                canvas?.drawLine(focusX, focusY, focusX, focusY+interval, mPaint) //left
                canvas?.drawLine(focusX, focusY + interval, focusX+interval, focusY + interval, mPaint) //bottom
                canvas?.drawLine(focusX+interval, focusY, focusX+interval, focusY + interval, mPaint) //right

                focusX += interval

              /*  Log.d("godgod","가로 그리는 중")*/
            }
            focusY += interval
            focusX = 0f

        /*    Log.d("godgod","세로 그리는 중")*/
        }

    }
}