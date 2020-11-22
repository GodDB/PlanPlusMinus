package com.example.todoplusminus.base

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController


/**
 * 모든 컨트롤러의 기초가 되는 Controller
 * */
abstract class ControllerBase : LifecycleController {

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    fun popCurrentController(){
        router.popCurrentController()
    }

    fun pushController(transaction : RouterTransaction){
        router.pushController(transaction)
    }

    fun popController(controller: Controller){
        router.popController(controller)
    }

    fun replaceController(transaction : RouterTransaction){
        router.replaceTopController(transaction)
    }



}