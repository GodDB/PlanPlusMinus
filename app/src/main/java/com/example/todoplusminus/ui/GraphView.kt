package com.example.todoplusminus.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import com.example.todoplusminus.databinding.UiGraphViewBinding
import com.example.todoplusminus.util.DpConverter
import kotlin.math.floor
import kotlin.math.max


class GraphView : LinearLayout {

    companion object {
        //현재는 y축에 보여지는 텍스트뷰의 갯수가 6개이다. 그러므로 y축 데이터는 6개를 넘어서는 안된다.
        private const val MAX_Y_AXIS_NUMBER = 6

        private val GRAPH_BAR_WIDTH = DpConverter.dpToPx(10f).toInt()
    }

    private lateinit var binder: UiGraphViewBinding
    private var maxYValue : Int = 0


    constructor(context: Context?) : super(context) {
        customInit(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        customInit(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        customInit(context)
    }

    fun setData(xDatas: List<String>, yDatas: List<Int>) {

        setDataYTVs(yDatas)

        binder.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binder.root.viewTreeObserver.removeGlobalOnLayoutListener(this)
                generateXTVs(xDatas)
                generateGraphBars(xDatas, yDatas)
            }
        })
    }


    private fun customInit(context: Context?) {
        binder = UiGraphViewBinding.inflate(LayoutInflater.from(context), this, true)
    }


    //X축 텍스트뷰의 갯수는 유동적이므로, 요구하는 x축에 따라 생성해낸다.
    private fun generateXTVs(list: List<String>) {
        val size = list.size - 1
        val width =
            binder.guideRight.x - binder.guideLeft.x - DpConverter.dpToPx(17f) //뷰는 좌상단을 기준으로 그려지기 때문에 item의 크기 한개만큼을 빼줘서 width로 산정한다.
        val itemInterval = (width / size)

        //generate textViews...
        var i = 0
        var x = binder.guideLeft.x
        val y = binder.bgView1.bottom.toFloat()

        while (i <= size) {
            val tv = TextView(binder.root.context).apply {
                this.x = x
                this.y = y
                text = list[i]
            }

            binder.root.addView(tv)
            i++
            x += itemInterval
        }
    }

    private fun generateGraphBars(xDatas: List<String>, yDatas: List<Int>) {

        val graphMaxHeight = binder.guideBottom.y - binder.guideTop.y

        val size = xDatas.size-1
        val width =
            binder.guideRight.x - binder.guideLeft.x - DpConverter.dpToPx(17f) //뷰는 좌상단을 기준으로 그려지기 때문에 item의 크기 한개만큼을 빼줘서 width로 산정한다.
        val itemInterval = (width / size)
        var x = binder.guideLeft.x

        var i = 0
        while (i <= size) {
            val graphHeight = (yDatas[i] / maxYValue.toFloat()) * graphMaxHeight
            val y = (binder.guideTop.y + graphMaxHeight-graphHeight)

            Log.d("godgod", "${yDatas[i]}   ${maxYValue}    ${graphMaxHeight}")

            val graphView = View(binder.root.context).apply {
                this.x = x
                this.y = y
                this.layoutParams = ViewGroup.LayoutParams(GRAPH_BAR_WIDTH, graphHeight.toInt())
                this.setBackgroundColor(Color.BLUE)
            }

            binder.root.addView(graphView)

            i++
            x += itemInterval
        }
    }

    private fun setDataYTVs(list: List<Int>) {
        val yTvs = listOf<TextView>(
            binder.valueY5,
            binder.valueY4,
            binder.valueY3,
            binder.valueY2,
            binder.valueY1,
            binder.valueY0
        )

        var maxValue = 0
        list.forEach { value ->
            maxValue = max(maxValue, value)
        }

        //등차수열 공비
        var commonRatio = 0f
        while (true) {
            commonRatio = getArithmeticalSequence(maxValue, MAX_Y_AXIS_NUMBER)
            //등차수열의 공비가 정수라면
            if (commonRatio == floor(commonRatio)) break

            //등차수열의 공비가 정수일때까지 반복한다.
            maxValue++
        }

        //max값을 저장한다.
        maxYValue = maxValue

        //새로 구한 max값을 기준으로 등차수열 공비만큼을 빼서 값을 채운다.
        yTvs.forEach {
            it.text = maxValue.toString()
            maxValue -= commonRatio.toInt()
        }
    }

    /**
     * 등차수열 공비를 구한다.
     *
     * 그래프의 y축 value는 0을 시작으로 등차수열로 나타난다.
     */
    private fun getArithmeticalSequence(max: Int, n: Int): Float {
        //등차수열 공비 = (끝항 - 첫항)/(n-1)
        return (max - 0) / (n - 1).toFloat()
    }

}