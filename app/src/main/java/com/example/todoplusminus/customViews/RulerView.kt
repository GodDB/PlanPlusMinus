package com.example.todoplusminus.customViews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.databinding.UiRulerViewBinding
import com.example.todoplusminus.databinding.UiRulerViewItem1Binding
import com.example.todoplusminus.databinding.UiRulerViewItem2Binding
import com.example.todoplusminus.util.VibrateHelper


class RulerView : FrameLayout {

    private lateinit var binder: UiRulerViewBinding
    private var mRulerData: List<Int>? = null
    var listener : OnRulerChangeListener? = null

    interface OnRulerChangeListener{
        fun onRulerChanging(value : Int){}
        fun onRulerChanged(value : Int){}
    }

    constructor(context: Context) : super(context) {
        customInit(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        customInit(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        customInit(context)
    }

    fun setRulerRange(range: IntRange) {
        val list: MutableList<Int> = mutableListOf()

        range.forEach {
            list.add(it)
        }

        mRulerData = list
        setRulerDataToAdapter(mRulerData!!)
    }

    fun scrollToValue(value: Int) {
        val index = searchIndexBy(value)

        if(index == -1) return

        binder.rulerList.smoothScrollToPosition(index)
    }

    private fun customInit(context: Context) {
        binder = UiRulerViewBinding.inflate(LayoutInflater.from(context), this, true)

        initRV()
    }

    private fun initRV() {
        binder.rulerList.adapter = RulerViewAdaper()
        binder.rulerList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val snapManager = PagerSnapManager()
        snapManager.attachToRvAndListener(binder.rulerList, object : PagerSnapManager.SnapChangeListener{
            override fun onPositionChanging(position: Int) {
                if(position != RecyclerView.NO_POSITION) listener?.onRulerChanging(mRulerData!![position])
                VibrateHelper.start()
            }
        })

        setRVPadding()
    }

    private fun setRVPadding() {
        binder.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binder.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val padding = binder.indicator.x.toInt()
                binder.rulerList.setPadding(padding, 0, padding, 0)
            }
        })
    }

    private fun searchIndexBy(value: Int): Int {
        mRulerData?.forEachIndexed { index, i ->
            if (i == value) return index
        }
        return -1
    }


    private fun setRulerDataToAdapter(list: List<Int>) {
        (binder.rulerList.adapter as? RulerViewAdaper)?.setRulerRange(list)
    }
}


class RulerViewAdaper : RecyclerView.Adapter<RulerVH>() {

    private val mRulerData: MutableList<Int> = mutableListOf()

    fun setRulerRange(list: List<Int>) {
        mRulerData.clear()
        mRulerData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerVH {
        return when {
            viewType % 5 == 0 -> {
                val vb = UiRulerViewItem2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LargeBarVH(vb)
            }
            else -> {
                val vb = UiRulerViewItem1Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SmallBarVH(vb)
            }
        }
    }

    override fun getItemCount(): Int = mRulerData.size

    override fun onBindViewHolder(holder: RulerVH, position: Int) {
        holder.bind(mRulerData[position])
    }

    override fun getItemViewType(position: Int): Int = position


    inner class SmallBarVH(private val vb: UiRulerViewItem1Binding) : RulerVH(vb.root) {
        override fun bind(data: Int) {

        }

    }

    inner class LargeBarVH(private val vb: UiRulerViewItem2Binding) : RulerVH(vb.root) {
        override fun bind(data: Int) {
            vb.rulerValue.text = data.toString()
        }
    }
}

abstract class RulerVH(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: Int)
}


/**
 * RulerView의 scroll 변경을 감지하는 object
 * */
class PagerSnapManager{

    private var snapPosition = RecyclerView.NO_POSITION
    private var mListener : SnapChangeListener? = null
    private var mRecyclerView : RecyclerView? = null
    private val snapHelper = PagerSnapHelper()

    interface SnapChangeListener{
        fun onPositionChanging(position : Int){}
        fun onPositionChanged(position: Int){}
    }

    fun attachToRvAndListener(rv : RecyclerView, listener : SnapChangeListener){
        mRecyclerView = rv
        mListener = listener
        snapHelper.attachToRecyclerView(rv)

        addEvent()
    }

    private fun addEvent(){
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val position = searchSnapPosition()
                if(position != snapPosition){
                    mListener?.onPositionChanging(position)
                    snapPosition = position
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    val position = searchSnapPosition()
                    if(position != snapPosition){
                        mListener?.onPositionChanging(position)
                        snapPosition = position
                    }
                }
            }
        })
    }

    private fun searchSnapPosition() : Int{
        val layoutManager = mRecyclerView?.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = snapHelper.findSnapView(layoutManager)  ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }
}