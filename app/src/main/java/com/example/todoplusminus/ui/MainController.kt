package com.example.todoplusminus.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.example.todoplusminus.R
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.databinding.ControllerMainBinding
import com.example.todoplusminus.db.PlannerDatabase
import com.example.todoplusminus.data.repository.PlannerRepository
import com.example.todoplusminus.data.repository.SettingRepository
import com.example.todoplusminus.data.repository.TrackerRepository
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.local.LocalDataSourceImpl
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.ui.main.PlannerController
import com.example.todoplusminus.ui.main.history.PlanHistoryController
import com.example.todoplusminus.ui.main.history.PlanHistoryVM
import com.example.todoplusminus.ui.main.memo.PlanMemoController
import com.example.todoplusminus.ui.setting.SettingController
import com.example.todoplusminus.ui.tracker.TrackerController
import com.example.todoplusminus.ui.tracker.TrackerVM

class MainController : VBControllerBase {

    private lateinit var binder: ControllerMainBinding

    private var auxRouter: Router? = null
    private lateinit var childRouter: Router

    constructor() : super()

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerMainBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()

        auxRouter = getChildRouter(binder.subArea)
        //popLastView를 true로 둠으로써, 마지막 컨트롤러 삭제시 뷰도 삭제된다. false이면 마지막컨트롤러 삭제시 뷰는 삭제 안됨.
        auxRouter?.setPopsLastView(true)
    }

    private fun configureUI() {
        configureBottomMenu()

        childRouter = getChildRouter(binder.mainArea)
        showMainPlannerEditor()
    }


    private fun configureBottomMenu() {
        binder.bottomMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.plannerItem -> showMainPlannerEditor()
                R.id.trackerItem -> showTrackerEditor()
                R.id.settingItem -> showSettingEditor()
            }
            true
        }
        binder.bottomMenu.setOnNavigationItemReselectedListener {
            false
        }
    }

    private fun showMainPlannerEditor() {
        pushControllerByTag(
            childRouter,
            RouterTransaction.with(
                PlannerController(plannerDelegate).apply {
                    retainViewMode = RetainViewMode.RETAIN_DETACH
                })
                .apply {
                    tag(PlannerController.TAG)
                    SimpleSwapChangeHandler()
                    SimpleSwapChangeHandler()
                },
            PlannerController.TAG
        )
    }

    private fun showTrackerEditor() {
        pushControllerByTag(
            childRouter,
            RouterTransaction.with(
                TrackerController().apply {
                    retainViewMode = RetainViewMode.RETAIN_DETACH
                }).apply {
                tag(TrackerController.TAG)
                SimpleSwapChangeHandler()
                SimpleSwapChangeHandler()
            },
            TrackerController.TAG
        )
    }

    private fun showSettingEditor() {
        pushControllerByTag(
            childRouter,
            RouterTransaction.with(
                SettingController().apply {
                    retainViewMode = RetainViewMode.RETAIN_DETACH
                }).apply {
                tag(SettingController.TAG)
                SimpleSwapChangeHandler()
                SimpleSwapChangeHandler()
            },
            SettingController.TAG
        )
    }


    private val plannerDelegate = object : PlannerController.Delegate {
        override fun showMemoEditor() {
            Log.d("godgod", "showMemoEditor()")
            auxRouter?.setRoot(
                RouterTransaction.with(
                    PlanMemoController()
                ).apply {
                    pushChangeHandler(VerticalChangeHandler())
                    popChangeHandler(VerticalChangeHandler())
                })
        }

        override fun showHistoryEditor(id: BaseID) {
            Log.d("godgod", "showHistoryEditor()")
            auxRouter?.setRoot(RouterTransaction.with(
                PlanHistoryController(id)
            ).apply {
                pushChangeHandler(VerticalChangeHandler())
                popChangeHandler(VerticalChangeHandler())
            })
        }

    }

}