package com.example.todoplusminus.ui.main.history.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.todoplusminus.R
import com.example.todoplusminus.base.BaseApplication

import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.databinding.ControllerPlanHistoryContentsBinding
import com.example.todoplusminus.databinding.ControllerPlanHistoryContentsItemBinding
import com.example.todoplusminus.util.ColorManager
import javax.inject.Inject

/**
 * plan history 화면에서 실제 contents(차트화면, summary) 담당하는 Controller
 * */
class PlanHistoryContentsController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(_mode: PlanHistoryContentVM.Mode, _targetId: BaseID) : super() {
        this._mode = _mode
        this._targetId = _targetId
    }

    private lateinit var _mode: PlanHistoryContentVM.Mode
    private lateinit var _targetId: BaseID

    @Inject
    lateinit var mPlanHistoryContentVM: PlanHistoryContentVM
    private lateinit var binder: ControllerPlanHistoryContentsBinding

    override fun connectDagger() {
        super.connectDagger()
        (activity?.application as BaseApplication).appComponent.planComponent().create()
            .historyComponent().create(_targetId).historyContentComponent().create(_mode).inject(this)
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanHistoryContentsBinding.inflate(inflater, container, false)
        binder.vm = mPlanHistoryContentVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        onSubscribe()
    }

    private fun configureUI() {

        binder.chartViewList.adapter =
            PlanHistoryChartAdapter()
        binder.chartViewList.layoutManager =
            LinearLayoutManager(binder.root.context, LinearLayoutManager.HORIZONTAL, true)
        snapHelper.attachToRecyclerView(binder.chartViewList)
    }

    private fun onSubscribe() {
        mPlanHistoryContentVM.mode.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { mode ->
                when (mode.toString()) {
                    PlanHistoryContentVM.Mode.MODE_WEEK -> {
                        setTextAverageTv(resources?.getString(R.string.week_average) ?: "")
                        setTextAccumalationTv(
                            resources?.getString(R.string.week_accumulation) ?: ""
                        )
                    }
                    PlanHistoryContentVM.Mode.MODE_MONTH -> {
                        setTextAverageTv(resources?.getString(R.string.month_average) ?: "")
                        setTextAccumalationTv(
                            resources?.getString(R.string.month_accumulation) ?: ""
                        )
                    }
                    PlanHistoryContentVM.Mode.MODE_YEAR -> {
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


    private val snapHelper = object : PagerSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager?,
            velocityX: Int,
            velocityY: Int
        ): Int {
            mPlanHistoryContentVM.summaryTargetIndex.value =
                super.findTargetSnapPosition(layoutManager, velocityX, velocityY)

            return super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        }
    }
}


class PlanHistoryChartAdapter() : RecyclerView.Adapter<PlanHistoryChartAdapter.PlanHistoryVH>() {

    private var mXDataList: List<List<String>> = mutableListOf()
    private var mYDataList: List<List<Int>> = mutableListOf()
    private var mTitleList: List<String> = mutableListOf()
    private var mGraphBarColor: Int = ColorManager.getRandomColor()

    /**
     * recyclerView에 데이터를 삽입한다.
     * */
    fun setData(
        xDataList: List<List<String>>,
        yDataList: List<List<Int>>,
        titleList: List<String>,
        graphBarColor: Int
    ) {
        this.mXDataList = xDataList
        this.mYDataList = yDataList
        this.mTitleList = titleList
        this.mGraphBarColor = graphBarColor
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanHistoryVH {
        val binder = ControllerPlanHistoryContentsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlanHistoryVH(binder)
    }

    override fun getItemCount(): Int = mYDataList.size

    override fun onBindViewHolder(holder: PlanHistoryVH, position: Int) {
        holder.setIsRecyclable(false) //view holder를 재활용하지 않는다.
        holder.bind()
    }


    inner class PlanHistoryVH(private val binder: ControllerPlanHistoryContentsItemBinding) :
        RecyclerView.ViewHolder(binder.root) {

        fun bind() {
            val position = adapterPosition
            binder.graphView.setData(mXDataList[position], mYDataList[position], mGraphBarColor)
            binder.graphTitle.text = mTitleList[position]
        }
    }
}
