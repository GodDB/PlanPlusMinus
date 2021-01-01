package com.example.todoplusminus.ui.main.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.R
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.base.GenericVH
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.ui.main.history.chart.PlanHistoryContentsController
import com.example.todoplusminus.databinding.ControllerPlanHistoryBinding
import com.example.todoplusminus.databinding.ControllerSettingTextviewItemBinding
import com.example.todoplusminus.databinding.ControllerSettingTitleItemBinding
import com.example.todoplusminus.databinding.ControllerSettingToggleItemBinding
import com.example.todoplusminus.ui.main.history.chart.PlanHistoryContentVM
import com.example.todoplusminus.ui.setting.*
import com.example.todoplusminus.util.ColorID
import com.example.todoplusminus.util.StringID
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class PlanHistoryController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(targetId: BaseID) {
        this._targetId = targetId
    }

    @Inject
    lateinit var mPlanHistoryVM: PlanHistoryVM

    private lateinit var _targetId: BaseID
    private lateinit var binder: ControllerPlanHistoryBinding


    override fun connectDagger() {
        super.connectDagger()

        (activity?.application as BaseApplication).appComponent.planComponent().create()
            .historyComponent().create(_targetId).inject(this)
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanHistoryBinding.inflate(inflater, container, false)
        binder.lifecycleOwner = this
        binder.vm = mPlanHistoryVM

        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUi()
        onSubscribe()
    }

    private fun configureUi() {
        configureTabLayout()
        initAlarmList()
    }

    private fun initAlarmList(){
        binder.alarmList.adapter = HistoryAlarmAdapter()
        binder.alarmList.layoutManager = LinearLayoutManager(binder.root.context)
    }

    private fun onSubscribe() {
        mPlanHistoryVM.wantEditorClose.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { isEnd ->
                if (isEnd) popCurrentController()
            }
        })

        mPlanHistoryVM.defaultAlarmData.observe(this, Observer { alarmDatas ->
            (binder.alarmList.adapter as? HistoryAlarmAdapter)?.setAlarmDatas(alarmDatas)
        })
    }

    private fun configureTabLayout() {
        val childRouter = getChildRouter(binder.containerHistoryContents)

        showWeekChart(childRouter)


        binder.historyTab.apply {
            addTab(newTab().setText(R.string.week), true)
            addTab(newTab().setText(R.string.month))
            addTab(newTab().setText(R.string.year))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {}

                override fun onTabUnselected(tab: TabLayout.Tab) {}

                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        //주별
                        0 -> showWeekChart(childRouter)
                        //월별
                        1 -> showMonthChart(childRouter)
                        //년별
                        2 -> showYearChart(childRouter)
                    }
                }
            })
        }


    }

    private fun showWeekChart(childRouter: Router) {
        val mode = PlanHistoryContentVM.Mode.createWeekMode()

        childRouter.setRoot(
            RouterTransaction.with(
                PlanHistoryContentsController(
                    mode, _targetId
                )
            )
        )
    }

    private fun showMonthChart(childRouter: Router) {
        val mode = PlanHistoryContentVM.Mode.createMonthMode()

        childRouter.setRoot(
            RouterTransaction.with(
                PlanHistoryContentsController(
                    mode, _targetId
                )
            )
        )
    }

    private fun showYearChart(childRouter: Router) {
        val mode = PlanHistoryContentVM.Mode.createYearMode()

        childRouter.setRoot(
            RouterTransaction.with(
                PlanHistoryContentsController(
                    mode, _targetId
                )
            )
        )
    }
}


class HistoryAlarmAdapter() : RecyclerView.Adapter<HistoryAlarmVH>() {

    class HistoryAlarmTitleVH(
        private val vb: ControllerSettingTitleItemBinding
    ) :
        HistoryAlarmVH(vb.root) {
        companion object {
            const val TYPE = 1
        }

        override fun bind(data: Triple<List<StringID>, ColorID, ValueData>) {
            data.first.forEach { stringID ->
                vb.titleTv.text = "${vb.titleTv.text} ${vb.root.context.getString(stringID.id)}"
            }

            //color
            vb.titleTv.setTextColor(ContextCompat.getColor(vb.root.context, data.second.id))
        }
    }

    class HistoryAlarmContainTextViewVH(
        private val vb: ControllerSettingTextviewItemBinding
    ) : HistoryAlarmVH(vb.root) {
        companion object {
            const val TYPE = 3
        }

        override fun bind(data: Triple<List<StringID>, ColorID, ValueData>) {
            val value = (data.third as? ValueString)?.value
            val tag = (data.third as? ValueString)?.tag
            vb.tag = tag

            data.first.forEach { stringID ->
                vb.titleTv.text = "${vb.titleTv.text} ${vb.root.context.getString(stringID.id)}"
            }
            vb.valueTv.text = value

            //color
            vb.titleTv.setTextColor(ContextCompat.getColor(vb.root.context, data.second.id))
        }

    }

    private var mDataList: MutableList<Triple<List<StringID>, ColorID, ValueData>> = mutableListOf()

    fun setAlarmDatas(datas: List<Triple<List<StringID>, ColorID, ValueData>>) {
        mDataList.clear()
        mDataList.addAll(datas)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = mDataList[position]
        return when (item.third) {
            is ValueEmpty -> HistoryAlarmTitleVH.TYPE
            is ValueString -> HistoryAlarmContainTextViewVH.TYPE
            //여기선 사용하지 않는다.. setting에서 사용하고 있는걸 재사용중이라서 ...
            is ValueBoolean -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAlarmVH {
        return when (viewType) {
            HistoryAlarmTitleVH.TYPE -> {
                val vb = ControllerSettingTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HistoryAlarmTitleVH(vb)
            }
            HistoryAlarmContainTextViewVH.TYPE -> {
                val vb = ControllerSettingTextviewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HistoryAlarmContainTextViewVH(vb)
            }
            else -> {
                val vb = ControllerSettingTextviewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HistoryAlarmContainTextViewVH(vb)
            }
        }
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: HistoryAlarmVH, position: Int) {
        holder.bind(mDataList[position])
    }
}
