package com.example.todoplusminus.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanHistoryBinding
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.vm.PlanHistoryVM

class PlanHistoryController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(repository: PlannerRepository){
        this.repository = repository
    }

    private var repository : PlannerRepository? = null
    private lateinit var binder : ControllerPlanHistoryBinding
    private var mVM : PlanHistoryVM? = null

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        mVM = PlanHistoryVM(repository!!)
        binder = ControllerPlanHistoryBinding.inflate(inflater, container, false)
        binder.lifecycleOwner = this
        binder.vm = mVM

        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUi()
        onSubscribe()
    }

    private fun configureUi(){

    }

    private fun onSubscribe(){
        mVM?.wantEditorClose?.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {isEnd ->
                if(isEnd) popCurrentController()
            }

        })
    }

}