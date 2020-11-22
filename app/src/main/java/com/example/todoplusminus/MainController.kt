package com.example.todoplusminus

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.controllers.PlannerController
import com.example.todoplusminus.controllers.SettingController
import com.example.todoplusminus.controllers.TrackerController
import com.example.todoplusminus.databinding.ControllerMainBinding

class MainController : VBControllerBase {

    private lateinit var binder: ControllerMainBinding

    private val mainRouter = router

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
    }


    private fun configureBottomMenu(){
        binder.bottomMenu.setOnNavigationItemSelectedListener {

            val childRouter = getChildRouter(binder.mainArea)

            when(it.itemId){
                R.id.plannerItem -> childRouter.setRoot(RouterTransaction.with(PlannerController()))
                R.id.trackerItem -> childRouter.setRoot(RouterTransaction.with(TrackerController()))
                R.id.settingItem -> childRouter.setRoot(RouterTransaction.with(SettingController()))
            }
            true
        }
    }

}