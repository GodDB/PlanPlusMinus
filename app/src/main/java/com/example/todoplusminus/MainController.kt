package com.example.todoplusminus

import android.util.Log
import android.view.KeyboardShortcutGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.controllers.PlanMemoController
import com.example.todoplusminus.controllers.PlannerController
import com.example.todoplusminus.controllers.SettingController
import com.example.todoplusminus.controllers.TrackerController
import com.example.todoplusminus.databinding.ControllerMainBinding
import com.example.todoplusminus.db.PlannerDatabase
import com.example.todoplusminus.repository.LocalDataSourceImpl
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.KeyboardDetector

class MainController : VBControllerBase {

    private lateinit var binder: ControllerMainBinding

    private lateinit var auxRouter : Router
    private lateinit var childRouter: Router

    //todo test ... 추후에 dagger로 교체
    private var plannerRepository : PlannerRepository? = null

    constructor() : super()

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerMainBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()

        auxRouter = Conductor.attachRouter(activity!!, binder.subArea, null)
    }

    private fun configureUI() {
        configureBottomMenu()

        //todo test
        val db = Room.databaseBuilder(
            applicationContext!!,
            PlannerDatabase::class.java,
            "plannerDB.sqlite3"
        ).build()

        val dataSource = LocalDataSourceImpl(db)
        plannerRepository = PlannerRepository(dataSource)


        childRouter = getChildRouter(binder.mainArea)
        pushControllerByTag(
            childRouter,
            RouterTransaction.with(PlannerController(plannerRepository!!, plannerDelegate)).apply {
                tag(PlannerController.TAG)
                SimpleSwapChangeHandler()
                SimpleSwapChangeHandler(false)
            },
            PlannerController.TAG
        )

    }


    private fun configureBottomMenu() {
        binder.bottomMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.plannerItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(PlannerController(plannerRepository!!, plannerDelegate)).apply {
                        tag(PlannerController.TAG)
                        SimpleSwapChangeHandler()
                        SimpleSwapChangeHandler(false)
                    },
                    PlannerController.TAG
                )
                R.id.trackerItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(TrackerController()).apply {
                        tag(TrackerController.TAG)
                        SimpleSwapChangeHandler()
                        SimpleSwapChangeHandler(false)
                    },
                    TrackerController.TAG
                )
                R.id.settingItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(SettingController()).apply {
                        tag(SettingController.TAG)
                        SimpleSwapChangeHandler()
                        SimpleSwapChangeHandler(false)
                    },
                    SettingController.TAG
                )
            }
            true
        }
    }


    private val plannerDelegate = object : PlannerController.Delegate{
        override fun showMemoEditor() {
            auxRouter.pushController(RouterTransaction.with(PlanMemoController(plannerRepository!!)).apply {
                pushChangeHandler(VerticalChangeHandler())
                popChangeHandler(VerticalChangeHandler(false))
            })
        }

        override fun showHistoryEditor() {

        }

    }

}