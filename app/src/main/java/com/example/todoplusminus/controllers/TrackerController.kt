package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerTrackerBinding

class TrackerController : DBControllerBase {

    private lateinit var binder : ControllerTrackerBinding

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_tracker, container, false)
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
    }
}