package com.example.todoplusminus.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanSizeEditorBinding
import com.example.todoplusminus.databinding.UiRulerViewBinding

class PlanSizeEditorController : VBControllerBase {

    interface Delegate{
        fun onChangingRuler()
        fun onChangedRuler()
    }

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(delegate : Delegate){
        this.mDelegate = delegate
    }

    private lateinit var binder : ControllerPlanSizeEditorBinding
    private var mDelegate : Delegate? = null

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanSizeEditorBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        val range = IntRange(8, 40)
        binder.rulerView.setRulerRange(range)
    }


}