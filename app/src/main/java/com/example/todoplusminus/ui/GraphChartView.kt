package com.example.todoplusminus.ui

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.renderscript.Sampler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.todoplusminus.R
import com.example.todoplusminus.databinding.UiGraphViewBinding
import com.example.todoplusminus.util.ColorManager
import com.example.todoplusminus.util.CommonAnimationHelper
import com.example.todoplusminus.util.DpConverter
import kotlin.math.floor
import kotlin.math.max


class GraphChartView : LinearLayout {

    companion object {
        //현재는 y축에 보여지는 텍스트뷰의 갯수가 6개이다. 그러므로 y축 데이터는 6개를 넘어서는 안된다.
        private const val MAX_Y_AXIS_NUMBER = 6

        private val GRAPH_BAR_WIDTH = DpConverter.dpToPx(10f).toInt()
    }

    private lateinit var binder: UiGraphViewBinding
    private var maxYValue : Int = 0
    private var mGraphViewColor = ColorManager.getRandomColor()


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

    fun setData(xDatas: List<String>, yDatas: List<Int>, bgColor : Int) {

        binder.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binder.root.viewTreeObserver.removeGlobalOnLayoutListener(this)
                mGraphViewColor = bgColor
                setDataYTVs(yDatas)
                generateXTVs(xDatas)
                generateGraphBars(yDatas)
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
                textSize = 12f
            }

            binder.root.addView(tv)
            i++
            x += itemInterval
        }
    }

    /**
     * 그래프바를 그려낸다.
     * */
    private fun generateGraphBars(yDatas: List<Int>) {

        //그래프의 최대 높이값을 기준으로 전달받은 yValue값을 나눠서 비율만큼 그래프의 높이를 정한다.
        val graphMaxHeight = binder.guideBottom.y - binder.guideTop.y

        //그래프의 간격은 차트의 넓이를 기준으로 갯수만큼 나눠서 그린다.
        val size = yDatas.size-1
        val width =
            binder.guideRight.x - binder.guideLeft.x - DpConverter.dpToPx(17f) //뷰는 좌상단을 기준으로 그려지기 때문에 item의 크기 한개만큼을 빼줘서 width로 산정한다.
        val itemInterval = (width / size)
        var x = binder.guideLeft.x

        var i = 0
        while (i <= size) {

            //그래프뷰 생성
            //count가 0인 것은 작업을 수행하지 않는다.
            if(yDatas[i] > 0){
                val graphView = CardView(binder.root.context).apply {
                    this.x = x + DpConverter.dpToPx(1.2f)
                    this.setCardBackgroundColor(ContextCompat.getColor(context, mGraphViewColor))
                    this.radius = 10f
                    //나머지 y나 height값들은 애니메이션을 통해서 값을 채워준다.
                }
                binder.root.addView(graphView)

                //그래프의 높이
                val graphHeight = (yDatas[i] / maxYValue.toFloat()) * graphMaxHeight
                //그래프가 그려질 y값 (차트의 y값을 기준으로 (그래프의 최대 높이 - 계산된 그래프의 높이값)만큼을 더해 구해준다.)
                val y = (binder.guideTop.y + graphMaxHeight-graphHeight)

                //run graph animation
                startGraphAnimation(graphView, y, graphHeight)
            }

            i++
            x += itemInterval
        }
    }

    private fun setDataYTVs(list: List<Int>) {
        //y축의 value값은 전달받은 값 중 가장 큰값을 기준으로 0까지 값이 지정된다 (등차수열)

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
            //현재 등차수열의 공비값을 구한다.
            commonRatio = getArithmeticalSequence(maxValue, MAX_Y_AXIS_NUMBER)
            //등차수열의 공비가 정수일때까지 반복한다.
            if (commonRatio == floor(commonRatio)) break

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

    /**
     * graphBar의 height와 y값을 이용해서 애니메이션을 실행한다.
     *
     * 결과물로는 사용자 눈에 그래프가 점점 길어져서 전달받은 y, height에 맞춰지는 애니메이션이다.
    * */
    private fun startGraphAnimation(v : View, y : Float, height : Float){
        //y값은 점점 줄어들며 상단에 위치한다. (뷰는 좌측 상단을 기준으로 그려지기 때문에)
        val yAnim = ValueAnimator.ofFloat(binder.guideBottom.y, y).apply {
            this.duration = 1000
            this.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                v.y = animatedValue
            }
        }

        //height 값은 점점 커진다.
        val heightAnim = ValueAnimator.ofInt(0, height.toInt()).apply {
            this.duration = 1000
            this.addUpdateListener {
                val animatedValue = it.animatedValue as Int
                val params = v.layoutParams.apply {
                    this.width = GRAPH_BAR_WIDTH
                    this.height = animatedValue
                }
                v.layoutParams = params
            }
        }

        //동시에 애니메이션 실행
        AnimatorSet().apply {
            this.playTogether(yAnim, heightAnim)
            start()
        }
    }


}