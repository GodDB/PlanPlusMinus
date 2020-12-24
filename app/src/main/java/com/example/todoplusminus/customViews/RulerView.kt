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


class RulerView : FrameLayout {

    private lateinit var binder : UiRulerViewBinding
    private var mRulerData : List<Int>? = null

    constructor(context: Context) : super(context){
        customInit(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        customInit(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        customInit(context)
    }

    fun setRulerRange(range : IntRange){
        val list : MutableList<Int> = mutableListOf()

        range.forEach {
            list.add(it)
        }

        mRulerData = list
        setRulerDataToAdapter(mRulerData!!)
    }

    private fun customInit(context : Context){
        binder = UiRulerViewBinding.inflate(LayoutInflater.from(context), this, true)

        initRV()
    }

    private fun initRV(){
        binder.rulerView.adapter = RulerViewAdaper()
        binder.rulerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        pagerSnapHelper.attachToRecyclerView(binder.rulerView)

        setRVPadding()
    }

    private fun setRVPadding(){
        binder.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                binder.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val padding = binder.indicator.x.toInt()
                binder.rulerView.setPadding(padding, 0, padding, 0)
            }
        })
    }


    private fun setRulerDataToAdapter(list : List<Int>){
        (binder.rulerView.adapter as? RulerViewAdaper)?.setRulerRange(list)
    }

    private val pagerSnapHelper = object : PagerSnapHelper(){
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager?,
            velocityX: Int,
            velocityY: Int
        ): Int {
            val pos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)

            //todo 표시 내일 하자
            if(pos != RecyclerView.NO_POSITION) Log.d("godgod", "${mRulerData!![pos]}")
            return pos
        }
    }
}



class RulerViewAdaper : RecyclerView.Adapter<RulerVH>() {

    private val mRulerData : MutableList<Int> = mutableListOf()

    fun setRulerRange(list : List<Int>){
        mRulerData.clear()
        mRulerData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerVH {
        return when {
            viewType % 5 == 0 -> {
                val vb = UiRulerViewItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                LargeBarVH(vb)
            }
            else -> {
                val vb = UiRulerViewItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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

abstract class RulerVH(view : View) : RecyclerView.ViewHolder(view){
    abstract fun bind(data : Int)
}