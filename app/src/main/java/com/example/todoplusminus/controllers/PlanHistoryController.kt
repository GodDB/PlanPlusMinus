package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.room.Room
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanHistoryBinding
import com.example.todoplusminus.db.PlannerDatabase
import com.example.todoplusminus.repository.LocalDataSourceImpl
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.vm.PlanHistoryContentVM
import com.example.todoplusminus.vm.PlanHistoryVM
import com.google.android.material.tabs.TabLayout

class PlanHistoryController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(vm: PlanHistoryVM) {
        this.mVM = vm
    }

    private lateinit var binder: ControllerPlanHistoryBinding
    private var mVM: PlanHistoryVM? = null


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanHistoryBinding.inflate(inflater, container, false)
        binder.lifecycleOwner = this
        binder.vm = mVM

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
        mVM?.wantEditorClose?.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { isEnd ->
                if (isEnd) popCurrentController()
            }

        })

        mVM?.planProject?.observe(this, Observer {
            it.getPlanDataList().forEach {
                Log.d("godgod", "${it.title}")
            }
        })
    }

    private fun configureTabLayout() {
        val childRouter = getChildRouter(binder.containerHistoryContents)

        //todo test
        val db = Room.databaseBuilder(
            applicationContext!!,
            PlannerDatabase::class.java,
            "plannerDB.sqlite3"
        ).build()
        val dataSource = LocalDataSourceImpl(db)
        val plannerRepository = PlannerRepository(dataSource)

        val mode = PlanHistoryContentVM.MODE_WEEK
        val vm = PlanHistoryContentVM(mode, plannerRepository)
        childRouter.setRoot(RouterTransaction.with(PlanHistoryContentsController(vm)))



        binder.historyTab.apply {
            addTab(newTab().setText(R.string.week), true)
            addTab(newTab().setText(R.string.month))
            addTab(newTab().setText(R.string.year) )

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    //todo tab ui 변경 로직

                    when (tab.position) {
                        //주별
                        0 -> {
                            Log.d("godgod", "주별")
                            val mode = PlanHistoryContentVM.MODE_WEEK
                            val vm = PlanHistoryContentVM(mode, plannerRepository)
                            childRouter.setRoot(RouterTransaction.with(PlanHistoryContentsController(vm)))
                        }
                        //월별
                        1 -> {
                            Log.d("godgod", "별")
                            val mode = PlanHistoryContentVM.MODE_MONTH
                            val vm = PlanHistoryContentVM(mode, plannerRepository)
                            childRouter.setRoot(RouterTransaction.with(PlanHistoryContentsController(vm)))
                        }
                        //년별
                        2 -> {
                            Log.d("godgod", "연별")
                            val mode = PlanHistoryContentVM.MODE_YEAR
                            val vm = PlanHistoryContentVM(mode, plannerRepository)
                            childRouter.setRoot(RouterTransaction.with(PlanHistoryContentsController(vm)))
                        }
                    }
                }

            })
        }
    }
}