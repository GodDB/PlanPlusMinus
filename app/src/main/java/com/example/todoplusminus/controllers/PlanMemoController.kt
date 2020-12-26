package com.example.todoplusminus.controllers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanMemoBinding
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.DeviceManager
import com.example.todoplusminus.vm.PlanMemoVM
import kotlin.math.max
import kotlin.math.min

class PlanMemoController : DBControllerBase {
    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(repository: PlannerRepository) {
        mRepository = repository
    }

    private var mRepository: PlannerRepository? = null
    private var mVM: PlanMemoVM? = null
    private lateinit var binder: ControllerPlanMemoBinding

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanMemoBinding.inflate(inflater, container, false)
        mVM = PlanMemoVM(mRepository!!)
        binder.vm = mVM
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
        mVM?.wantEditorClose?.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { wantEditorClose ->
                if (wantEditorClose) {
                    hideKeypad()
                    popCurrentController()
                }
            }
        })

        mVM?.showWarningDeleteDialog?.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { show ->
                if(show) showWarningDialog()
            }
        })
    }

    private fun showWarningDialog() {
        val title = binder.rootView.context.getString(R.string.delete_memo_text)
        val dialog = CommonDialogController(
            titleText = title,
            delegate = object : CommonDialogController.Delegate {
                override fun onComplete() {
                    mVM?.onDelete()
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