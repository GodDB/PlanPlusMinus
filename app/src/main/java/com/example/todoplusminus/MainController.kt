package com.example.todoplusminus

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.controllers.*
import com.example.todoplusminus.databinding.ControllerMainBinding
import com.example.todoplusminus.db.PlannerDatabase
import com.example.todoplusminus.repository.LocalDataSourceImpl
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.vm.PlanHistoryVM

class MainController : VBControllerBase {

    private lateinit var binder: ControllerMainBinding

    private var auxRouter : Router? = null
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

        auxRouter = getChildRouter(binder.subArea)
        //popLastView를 true로 둠으로써, 마지막 컨트롤러 삭제시 뷰도 삭제된다. false이면 마지막컨트롤러 삭제시 뷰는 삭제 안됨.
        auxRouter?.setPopsLastView(true)
    }

    private fun configureUI() {
        configureBottomMenu()

        //todo test
        val db = Room.databaseBuilder(
            applicationContext!!,
            PlannerDatabase::class.java,
            "plannerDB.sqlite3"
        ).createFromAsset("pre_planDB").build()

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
                        SimpleSwapChangeHandler(false)
                        SimpleSwapChangeHandler(false)
                    },
                    PlannerController.TAG
                )
                R.id.trackerItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(TrackerController()).apply {
                        tag(TrackerController.TAG)
                        SimpleSwapChangeHandler(false)
                        SimpleSwapChangeHandler(false)
                    },
                    TrackerController.TAG
                )
                R.id.settingItem -> pushControllerByTag(
                    childRouter,
                    RouterTransaction.with(SettingController()).apply {
                        tag(SettingController.TAG)
                        SimpleSwapChangeHandler(false)
                        SimpleSwapChangeHandler()
                    },
                    SettingController.TAG
                )
            }
            true
        }
    }


    private val plannerDelegate = object : PlannerController.Delegate{
        override fun showMemoEditor() {
            Log.d("godgod", "showMemoEditor()")
            auxRouter?.setRoot(RouterTransaction.with(PlanMemoController(plannerRepository!!)).apply {
                pushChangeHandler(VerticalChangeHandler())
                popChangeHandler(VerticalChangeHandler())
            })
        }

        override fun showHistoryEditor(id : String) {
            Log.d("godgod", "showHistoryEditor()")
            val vm = PlanHistoryVM(id, plannerRepository!!)
            auxRouter?.setRoot(RouterTransaction.with(PlanHistoryController(vm)).apply {
                pushChangeHandler(VerticalChangeHandler())
                popChangeHandler(VerticalChangeHandler())
            })
        }

    }

}