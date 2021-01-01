package com.example.todoplusminus.ui.main.history.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoplusminus.base.DBControllerBase

class PlanHistoryAlarmController : DBControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    override fun connectDagger() {
        super.connectDagger()
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        TODO("Not yet implemented")
    }

    override fun onViewBound(v: View) {
        TODO("Not yet implemented")
    }
}