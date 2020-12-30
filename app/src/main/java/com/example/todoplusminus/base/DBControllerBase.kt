package com.example.todoplusminus.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 데이터바인딩을 사용하는 Controller를 위한 baseController
 * */
abstract class DBControllerBase : ControllerBase {

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        connectDagger()
        val v = connectDataBinding(inflater, container)
        onViewBound(v)
        return v
    }

    abstract fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup) : View

    abstract fun onViewBound(v : View)
}