package com.example.todoplusminus.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanMemoBinding
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.vm.PlanMemoVM

class PlanMemoController : DBControllerBase {
    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(repository: PlannerRepository){
        mRepository = repository
    }

    private var mRepository : PlannerRepository? = null
    private var mVM : PlanMemoVM? = null
    private lateinit var binder : ControllerPlanMemoBinding

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanMemoBinding.inflate(inflater, container, false)
        mVM = PlanMemoVM(mRepository!!)
        binder.vm = mVM
        binder.lifecycleOwner = this

        return binder.root
    }

    override fun onViewBound(v: View) {
        onSubscribe()
    }

    private fun onSubscribe(){

    }
}