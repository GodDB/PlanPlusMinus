package com.example.todoplusminus.ui

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.example.todoplusminus.R
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.databinding.ControllerMainBinding
import com.example.todoplusminus.ui.common.CommonDialogController
import com.example.todoplusminus.ui.main.MainPlannerController
import com.example.todoplusminus.ui.main.history.PlanHistoryController
import com.example.todoplusminus.ui.main.memo.PlanMemoController
import com.example.todoplusminus.ui.setting.SettingController
import com.example.todoplusminus.ui.tracker.TrackerController
import java.lang.Exception

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

    /**
     * back press event
     * */
    override fun handleBack(): Boolean {
        //back으로 인한 컨트롤러를 pop 시킨 후
        val result = super.handleBack()

        //pop된 백스택에서 가장 최상단을 구해 bottomSheet icon을 변경한다.
        val targetTag = getTopTransactionTagToChildRouter() ?: return result
        val bottomId = convertTransactionTagToBottomId(targetTag)
        binder.bottomMenu.menu.findItem(bottomId).isChecked = true

        return result
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

    private fun showExitDialog() : Boolean {
        var result : Boolean = false

        val dialogController = CommonDialogController(
            delegate = object : CommonDialogController.Delegate {
                override fun onComplete() {
                    result = true
                    auxRouter?.popCurrentController()
                }

                override fun onCancel() {
                    result = false
                    auxRouter?.popCurrentController()
                }
            },
            titleText = binder.root.context.getString(R.string.want_to_quit_the_app)
        )

        auxRouter?.setRoot(RouterTransaction.with(dialogController)
            .apply {
                pushChangeHandler(FadeChangeHandler(false))
                popChangeHandler(FadeChangeHandler())
            }
        )

        return result
    }

    private fun showMainPlannerEditor() {
        pushControllerByTag(
            childRouter,
            RouterTransaction.with(
                MainPlannerController(plannerDelegate).apply {
                    retainViewMode = RetainViewMode.RETAIN_DETACH
                })
                .apply {
                    tag(MainPlannerController.TAG)
                    SimpleSwapChangeHandler()
                    SimpleSwapChangeHandler()
                },
            MainPlannerController.TAG
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


    //child router의 백스택에서 최상단에 위치한 transaction의 tag를 리턴한다.
    fun getTopTransactionTagToChildRouter() : String?{
        return childRouter.backstack.lastOrNull()?.tag()
    }

    /**
     * transaction의 tag값으로 해당 bottom navigation의 id로 변환한다.
     * */
    private fun convertTransactionTagToBottomId(tag : String) : Int{
        return when(tag){
            MainPlannerController.TAG -> R.id.plannerItem
            SettingController.TAG -> R.id.settingItem
            TrackerController.TAG -> R.id.trackerItem
            else -> throw Exception()
        }
    }



    private val plannerDelegate = object : MainPlannerController.Delegate {
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