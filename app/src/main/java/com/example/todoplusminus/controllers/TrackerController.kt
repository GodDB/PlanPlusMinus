package com.example.todoplusminus.controllers

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerTrackerBinding
import com.example.todoplusminus.databinding.ControllerTrackerItemBinding
import com.example.todoplusminus.databinding.ControllerTrackerItemContentBinding
import com.example.todoplusminus.util.LocalDateRange
import com.example.todoplusminus.vm.TrackerData
import com.example.todoplusminus.vm.TrackerVM
import nl.dionsegijn.konfetti.models.Shape
import java.time.LocalDate
import java.util.*

class TrackerController : DBControllerBase {

    companion object {
        const val TAG = "tracker_controller"
    }

    private lateinit var binder: ControllerTrackerBinding
    private var mTrackerVM : TrackerVM? = null

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(trackerVM : TrackerVM){
        mTrackerVM = trackerVM
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_tracker, container, false)
        binder.lifecycleOwner = this
        binder.vm = mTrackerVM

        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        onSubScribe()
    }

    private fun configureUI() {
        binder.trackerList.adapter = TrackerListAdapter()
        binder.trackerList.layoutManager =
            LinearLayoutManager(binder.root.context, LinearLayoutManager.HORIZONTAL, true)

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binder.trackerList)
    }

    private fun onSubScribe() {
        mTrackerVM?.trackerDataMap?.observe(this, androidx.lifecycle.Observer {
            (binder.trackerList.adapter as? TrackerListAdapter)?.setTrackerDataMap(it)
        })
    }

}

class TrackerListAdapter : RecyclerView.Adapter<TrackerListAdapter.TrackerVM>() {

    private val mTrackerDataMap: MutableMap<LocalDateRange, List<TrackerData>> = mutableMapOf()
    private val mTrackerKeyList: MutableList<LocalDateRange> = mutableListOf()

    fun setTrackerDataMap(map: Map<LocalDateRange, List<TrackerData>>) {
        mTrackerDataMap.clear()
        mTrackerDataMap.putAll(map)
        mTrackerKeyList.clear()
        mTrackerKeyList.addAll(mTrackerDataMap.keys.toList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackerVM {
        val vb =
            ControllerTrackerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackerVM(vb)
    }

    override fun getItemCount(): Int = mTrackerDataMap.size

    override fun onBindViewHolder(holder: TrackerVM, position: Int) {
        holder.bind()
    }

    inner class TrackerVM(private val vb: ControllerTrackerItemBinding) :
        RecyclerView.ViewHolder(vb.root) {
        init {
            vb.trackerContent.adapter = TrackerContentAdapter()
            vb.trackerContent.layoutManager = LinearLayoutManager(vb.root.context)
        }

        fun bind() {
            val key: LocalDateRange = mTrackerKeyList[adapterPosition]

            vb.dateRangeTv.text = key.getMMDD()

            (vb.trackerContent.adapter as? TrackerContentAdapter)?.setTrackerContentList(
                mTrackerDataMap[key] ?: mutableListOf()
            )
        }
    }
}

class TrackerContentAdapter() : RecyclerView.Adapter<TrackerContentAdapter.TrackerContentVM>() {

    private val trackerContentList: MutableList<TrackerData> = mutableListOf()

    fun setTrackerContentList(datas: List<TrackerData>) {
        trackerContentList.clear()
        trackerContentList.addAll(datas)
        notifyDataSetChanged()
    }

    inner class TrackerContentVM(private val vb: ControllerTrackerItemContentBinding) :
        RecyclerView.ViewHolder(vb.root) {

        private val checkViewList = listOf(
            vb.mondayCheck,
            vb.tuesdayCheck,
            vb.wednesdayCheck,
            vb.thursdayCheck,
            vb.fridayCheck,
            vb.saturdayCheck,
            vb.sundayCheck
        )

        fun bind() {
            val trackerData = trackerContentList[adapterPosition]

            vb.contentName.text = trackerData.name
            setDataToCheckViews(trackerData)
        }

        private fun setDataToCheckViews(trackerData: TrackerData) {
            val countList = listOf(
                trackerData.mondayCount,
                trackerData.tuesdayCount,
                trackerData.wednesdayCount,
                trackerData.thursdayCount,
                trackerData.fridayCount,
                trackerData.saturdayCount,
                trackerData.sundayCount
            )

            countList.forEachIndexed { index, count->
                if(count >0){
                    checkViewList[index].setCardBackgroundColor(ContextCompat.getColor(vb.root.context, trackerData.bgColor))
                    checkViewList[index].visibility = View.VISIBLE
                }
                else checkViewList[index].visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackerContentVM {
        val vb = ControllerTrackerItemContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackerContentVM(vb)
    }

    override fun getItemCount(): Int = trackerContentList.size

    override fun onBindViewHolder(holder: TrackerContentVM, position: Int) {
        holder.bind()
    }
}
