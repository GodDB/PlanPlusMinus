package com.example.todoplusminus

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.controllers.*
import com.example.todoplusminus.databinding.ControllerMainBinding
import com.example.todoplusminus.db.PlannerDatabase
import com.example.todoplusminus.repository.*
import com.example.todoplusminus.repository.SharedPrefManager
import com.example.todoplusminus.util.DeviceManager
import com.example.todoplusminus.vm.*

class MainController : VBControllerBase {

    private lateinit var binder: ControllerMainBinding

    private var auxRouter: Router? = null
    private lateinit var childRouter: Router

    //todo test ... 추후에 dagger로 교체
    private var plannerRepository: PlannerRepository? = null

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

    private fun showMainPlannerEditor(){
        //todo test
        val db = PlannerDatabase.getInstance(applicationContext!!)

        val dataSource = LocalDataSourceImpl(db)
        val sharedPrefManager =
            SharedPrefManager(
                applicationContext!!
            )
        plannerRepository = PlannerRepository(dataSource, sharedPrefManager)

        pushControllerByTag(
            childRouter,
            RouterTransaction.with(
                PlannerController(
                    plannerRepository!!,
                    plannerDelegate
                ).apply { retainViewMode = RetainViewMode.RETAIN_DETACH })
                .apply {
                    tag(PlannerController.TAG)
                    SimpleSwapChangeHandler()
                    SimpleSwapChangeHandler()
                },
            PlannerController.TAG
        )
    }

    private fun showTrackerEditor(){
        val db = PlannerDatabase.getInstance(applicationContext!!)
        val dataSource = LocalDataSourceImpl(db)
        val trackerRepo = TrackerRepository(dataSource)
        val trackerVM = TrackerVM(trackerRepo)

        pushControllerByTag(
            childRouter,
            RouterTransaction.with(TrackerController(trackerVM).apply {
                retainViewMode = RetainViewMode.RETAIN_DETACH
            }).apply {
                tag(TrackerController.TAG)
                SimpleSwapChangeHandler()
                SimpleSwapChangeHandler()
            },
            TrackerController.TAG
        )
    }

    private fun showSettingEditor(){
        //todo replace dagger
        val db = PlannerDatabase.getInstance(applicationContext!!)

        val dataSource = LocalDataSourceImpl(db)
        val sharedPrefManager =
            SharedPrefManager(
                applicationContext!!
            )
        val fontManager = FontDownloadManager(applicationContext!!)
        val settingRepo = SettingRepository(sharedPrefManager, fontManager, dataSource)

        pushControllerByTag(
            childRouter,
            RouterTransaction.with(SettingController(settingRepo).apply {
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
                RouterTransaction.with(PlanMemoController(plannerRepository!!)).apply {
                    pushChangeHandler(VerticalChangeHandler())
                    popChangeHandler(VerticalChangeHandler())
                })
        }

        override fun showHistoryEditor(id: String) {
            Log.d("godgod", "showHistoryEditor()")
            val vm = PlanHistoryVM(id, plannerRepository!!)
            auxRouter?.setRoot(RouterTransaction.with(PlanHistoryController(vm)).apply {
                pushChangeHandler(VerticalChangeHandler())
                popChangeHandler(VerticalChangeHandler())
            })
        }

    }

}