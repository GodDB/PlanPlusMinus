package com.example.todoplusminus.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerFontSettingBinding
import com.example.todoplusminus.vm.FontSettingVM

class FontSettingController : DBControllerBase {
    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(fontSettingVM: FontSettingVM) {
        this.mFontSettingVM = fontSettingVM
    }

    private lateinit var mFontSettingVM: FontSettingVM
    private lateinit var binder: ControllerFontSettingBinding


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerFontSettingBinding.inflate(inflater, container, false)
        binder.vm = mFontSettingVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        onSubscribe()
    }

    private fun configureUI() {

    }

    private fun onSubscribe() {

    }


}