package com.example.todoplusminus.ui.main.memo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.R
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanMemoBinding
import com.example.todoplusminus.ui.common.CommonDialogController
import java.time.LocalDate
import javax.inject.Inject

class PlanMemoController : DBControllerBase {
    constructor() : super()
    constructor(args: Bundle?) : super(args)
/*    constructor(repository: PlannerRepository) {
        mRepository = repository
    }*/

    @Inject
    lateinit var mPlanMemoVM : PlanMemoVM
    private lateinit var binder: ControllerPlanMemoBinding

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        (activity?.application as BaseApplication).appComponent.planComponent().create().memoComponent().create(
            LocalDate.now()).inject(this)

        binder = ControllerPlanMemoBinding.inflate(inflater, container, false)
/*        mVM = PlanMemoVM(mRepository!!)*/
        binder.vm = mPlanMemoVM
        binder.lifecycleOwner = this

        return binder.root
    }

    override fun onViewBound(v: View) {
        addEvent()
        onSubscribe()
    }

    private fun addEvent() {

    }

    private fun onSubscribe() {
        mPlanMemoVM.wantEditorClose.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { wantEditorClose ->
                if (wantEditorClose) {
                    hideKeypad()
                    popCurrentController()
                }
            }
        })

        mPlanMemoVM.showWarningDeleteDialog.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { show ->
                if(show) showWarningDialog()
            }
        })
    }

    private fun showWarningDialog() {
        val title = binder.rootView.context.getString(R.string.delete_memo_text)
        val dialog = CommonDialogController(
            titleText = title,
            delegate = object :
                CommonDialogController.Delegate {
                override fun onComplete() {
                    mPlanMemoVM.onDelete()
                    popCurrentController()
                }

                override fun onCancel() {
                    popCurrentController()
                }
            })

        this.pushController(RouterTransaction.with(dialog))
    }

    private fun hideKeypad() {
        val inputManager =
            binder.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}