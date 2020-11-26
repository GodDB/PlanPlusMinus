package com.example.todoplusminus

import android.util.Log
import android.view.KeyboardShortcutGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.controllers.PlannerController
import com.example.todoplusminus.controllers.SettingController
import com.example.todoplusminus.controllers.TrackerController
import com.example.todoplusminus.databinding.ControllerMainBinding
import com.example.todoplusminus.util.KeyboardDetector

class MainController : VBControllerBase {

    private lateinit var binder: ControllerMainBinding

    private val mainRouter = router
    private lateinit var childRouter: Router

    constructor() : super()

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerMainBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
    }

    private fun configureUI() {
        configureBottomMenu()

        childRouter = getChildRouter(binder.mainArea)
        pushControllerByTag(
            childRouter,
            RouterTransaction.with(PlannerController()).apply {
                tag(PlannerController.TAG)
                retainViewMode = RetainViewMode.RETAIN_DETACH
            },
            PlannerController.TAG
        )

    }


    private fun configureBottomMenu() {
        binder.bottomMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.plannerItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(PlannerController()).apply {
                        tag(PlannerController.TAG)
                        retainViewMode = RetainViewMode.RETAIN_DETACH
                    },
                    PlannerController.TAG
                )
                R.id.trackerItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(TrackerController()).apply {
                        tag(TrackerController.TAG)
                        retainViewMode = RetainViewMode.RETAIN_DETACH
                    },
                    TrackerController.TAG
                )
                R.id.settingItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(SettingController()).apply {
                        tag(SettingController.TAG)
                        retainViewMode = RetainViewMode.RETAIN_DETACH
                    },
                    SettingController.TAG
                )
            }
            true
        }
    }



}