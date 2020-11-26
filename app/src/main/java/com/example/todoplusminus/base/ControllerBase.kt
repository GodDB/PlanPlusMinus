package com.example.todoplusminus.base

import android.os.Bundle
import android.util.Log
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler


/**
 * 모든 컨트롤러의 기초가 되는 Controller
 * */
abstract class ControllerBase : LifecycleController {

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    fun popCurrentController() {
        router.popCurrentController()
    }

    fun pushController(transaction: RouterTransaction) {
        router.pushController(transaction)
    }

    fun popController(controller: Controller) {
        router.popController(controller)
    }

    fun replaceController(transaction: RouterTransaction) {
        router.replaceTopController(transaction)
    }


    /**
     * controller의 tag를 이용해서 백스택에 push한다.
     *
     * 백스택 내부에 동일한 tag가 존재한다면 상단에 노출되고,
     * tag가 존재하지 않는다면 push된다.
     * */
    fun pushControllerByTag(router: Router, newTransaction: RouterTransaction, newTag: String) {
        val backstack = router.backstack

        backstack.forEachIndexed { index, preTransaction ->
            if(preTransaction.tag() == newTag){
                val tempTransaction = backstack[index]
                backstack.removeAt(index)
                backstack.add(tempTransaction)
                router.setBackstack(backstack, null)
                return
            }
        }
        router.pushController(newTransaction)

    }


}