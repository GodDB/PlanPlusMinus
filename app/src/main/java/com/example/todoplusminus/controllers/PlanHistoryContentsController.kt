package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import com.example.todoplusminus.R

import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanHistoryContentsBinding
import com.example.todoplusminus.databinding.ControllerPlanHistoryContentsItemBinding
import com.example.todoplusminus.util.ColorManager
import com.example.todoplusminus.vm.PlanHistoryContentVM
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * plan history 화면에서 차트화면을 담당하는 Controller
 * */
class PlanHistoryContentsController : DBControllerBase {

    private var mVm: PlanHistoryContentVM? = null
    private lateinit var binder: ControllerPlanHistoryContentsBinding

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(vm: PlanHistoryContentVM) : super() {
        mVm = vm
    }


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanHistoryContentsBinding.inflate(inflater, container, false)
        binder.vm = mVm
        binder.lifecycleOwner = this


        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        onSubscribe()
    }

    private fun configureUI() {

        binder.chartViewList.adapter = PlanHistoryChartAdapter()
        binder.chartViewList.layoutManager = LinearLayoutManager(binder.root.context, LinearLayoutManager.HORIZONTAL, true)
        PagerSnapHelper().attachToRecyclerView(binder.chartViewList)
    }

    private fun onSubscribe() {
        mVm?.mode?.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { mode ->
                when (mode) {
                    PlanHistoryContentVM.MODE_WEEK -> {
                        setTextAverageTv(resources?.getString(R.string.week_average) ?: "")
                        setTextAccumalationTv(
                            resources?.getString(R.string.week_accumulation) ?: ""
                        )
                    }
                    PlanHistoryContentVM.MODE_MONTH -> {
                        setTextAverageTv(resources?.getString(R.string.month_average) ?: "")
                        setTextAccumalationTv(
                            resources?.getString(R.string.month_accumulation) ?: ""
                        )
                    }
                    PlanHistoryContentVM.MODE_YEAR -> {
                        setTextAverageTv(resources?.getString(R.string.year_average) ?: "")
                        setTextAccumalationTv(
                            resources?.getString(R.string.year_accumulation) ?: ""
                        )
                    }
                }
            }
        })
    }

    private fun setTextAverageTv(text: String) {
        binder.averageTitle.text = text
    }

    private fun setTextAccumalationTv(text: String) {
        binder.accumulateTitle.text = text
    }
}

class PlanHistoryChartAdapter() : RecyclerView.Adapter<PlanHistoryChartAdapter.PlanHistoryVH>() {

    private var mXDataList : List<List<String>> = mutableListOf()
    private var mYDataList : List<List<Int>> = mutableListOf()
    private var mTitleList : List<String> = mutableListOf()
    private var mGraphBarColor : Int = ColorManager.getRandomColor()

    /**
     * recyclerView에 데이터를 삽입한다.
     *
     * 최신순으로 보여주기 위해 전달된 데이터에 reverse를 실행한다.
     * */
    fun setData(xDataList : List<List<String>> ,yDataList : List<List<Int>>, titleList : List<String>, graphBarColor : Int){
        this.mXDataList = xDataList.reversed()
        this.mYDataList = yDataList.reversed()
        this.mTitleList = titleList.reversed()
        this.mGraphBarColor = graphBarColor
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanHistoryVH {
        val binder = ControllerPlanHistoryContentsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanHistoryVH(binder)
    }

    override fun getItemCount(): Int = mYDataList.size

    override fun onBindViewHolder(holder: PlanHistoryVH, position: Int) {
        holder.setIsRecyclable(false) //view holder를 재활용하지 않는다.
        holder.bind()
    }


    inner class PlanHistoryVH(private val binder : ControllerPlanHistoryContentsItemBinding) : RecyclerView.ViewHolder(binder.root){

        fun bind(){
            val position = adapterPosition
            binder.graphView.setData(mXDataList[position], mYDataList[position], mGraphBarColor)
            binder.graphTitle.text = mTitleList[position]
        }
    }
}
