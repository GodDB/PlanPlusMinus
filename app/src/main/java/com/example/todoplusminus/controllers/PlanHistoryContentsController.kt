package com.example.todoplusminus.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import com.example.todoplusminus.R

import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanHistoryContentsBinding
import com.example.todoplusminus.vm.PlanHistoryContentVM
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * plan history 화면에서 차트화면을 담당하는 Controller
 * */
class PlanHistoryContentsController : DBControllerBase {

    private var mVm : PlanHistoryContentVM? = null
    private lateinit var binder : ControllerPlanHistoryContentsBinding

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(vm : PlanHistoryContentVM) : super(){
        mVm = vm
    }


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanHistoryContentsBinding.inflate(inflater, container, false)
        binder.vm = mVm
        binder.lifecycleOwner = this


        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        onSubscribe()
    }

    private fun configureUI(){
    }

    private fun onSubscribe(){
        mVm?.mode?.observe(this, Observer {
            when(it){
                PlanHistoryContentVM.MODE_WEEK -> {
                    setTextAverageTv(resources?.getString(R.string.week_average) ?: "")
                    setTextAccumalationTv(resources?.getString(R.string.week_accumulation) ?: "")
                }
                PlanHistoryContentVM.MODE_MONTH -> {
                    setTextAverageTv(resources?.getString(R.string.month_average) ?: "")
                    setTextAccumalationTv(resources?.getString(R.string.month_accumulation) ?: "")
                }
                PlanHistoryContentVM.MODE_YEAR -> {
                    setTextAverageTv(resources?.getString(R.string.year_average) ?: "")
                    setTextAccumalationTv(resources?.getString(R.string.year_accumulation) ?: "")
                }
            }
        })
    }

    private fun setTextAverageTv(text : String){
        binder.averageTitle.text = text
    }
    private fun setTextAccumalationTv(text : String){
        binder.accumulateTitle.text = text
    }


}
