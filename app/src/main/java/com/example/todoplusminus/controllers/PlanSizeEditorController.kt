package com.example.todoplusminus.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.customViews.RulerView
import com.example.todoplusminus.databinding.ControllerPlanSizeEditorBinding

class PlanSizeEditorController : VBControllerBase {

    interface Delegate{
        fun onComplete(value : Int)
        fun onCancel()
    }

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(delegate : Delegate, _planSize : Int = AppConfig.planSize){
        this.mDelegate = delegate
        curPlanSize = _planSize
    }

    private lateinit var binder : ControllerPlanSizeEditorBinding
    private var mDelegate : Delegate? = null
    private var curPlanSize : Int = 12

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanSizeEditorBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        val range = IntRange(8, 40)
        binder.rulerView.setRulerRange(range)
        binder.rulerView.scrollToValue(curPlanSize)

        binder.rulerView.listener = rulerListener

        addEvent()
    }

    private fun addEvent(){
        binder.btnCancel.setOnClickListener {
            mDelegate?.onCancel()
        }
        binder.btnOnComplete.setOnClickListener {
            mDelegate?.onComplete(curPlanSize)
        }
    }

    private val rulerListener = object : RulerView.OnRulerChangeListener{
        override fun onRulerChanging(value: Int) {
            curPlanSize = value
        }
    }


}