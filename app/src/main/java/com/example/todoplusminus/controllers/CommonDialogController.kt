package com.example.todoplusminus.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.databinding.ControllerCommonDialogBinding

class CommonDialogController : VBControllerBase {

    interface Delegate{
        fun onComplete()
        fun onCancel()
    }

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(delegate : Delegate, titleText : String){
        mDelegate = delegate
        mTitleText = titleText
    }

    private lateinit var binder : ControllerCommonDialogBinding
    private var mDelegate : Delegate? = null
    private var mTitleText : String = ""

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerCommonDialogBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        binder.dialogContent.text = mTitleText
        addEvent()
    }

    private fun addEvent(){
        binder.btnDone.setOnClickListener { mDelegate?.onComplete() }
        binder.btnCancel.setOnClickListener { mDelegate?.onCancel() }
    }
}