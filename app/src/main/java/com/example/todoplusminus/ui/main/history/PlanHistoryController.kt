package com.example.todoplusminus.ui.main.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.R
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.ui.main.history.chart.PlanHistoryContentsController
import com.example.todoplusminus.databinding.ControllerPlanHistoryBinding
import com.example.todoplusminus.ui.main.history.chart.PlanHistoryContentVM
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
    }

    private fun onSubscribe() {
        mPlanHistoryVM.wantEditorClose.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { isEnd ->
                if (isEnd) popCurrentController()
            }

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
