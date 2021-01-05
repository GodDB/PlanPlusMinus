package com.example.todoplusminus.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.ui.MainController
import com.example.todoplusminus.base.BaseApplication

import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.databinding.ControllerSplashBinding
import com.example.todoplusminus.util.*
import kotlinx.coroutines.*
import javax.inject.Inject

class SplashController : VBControllerBase {

    private lateinit var binder: ControllerSplashBinding

    @Inject
    lateinit var splashVM: SplashVM

    constructor() : super()
    constructor(args: Bundle?) : super(args)
  /*  constructor(splashVM : SplashVM){
        this.splashVM = splashVM
    }*/

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerSplashBinding.inflate(inflater, container, false)

        (activity?.application as BaseApplication).appComponent.splashComponent().create().inject(this)

        return binder.root
    }

    override fun onViewBound(v: View) {

        CoroutineScope(Dispatchers.Main).launch {
            initialize()
            replaceController(RouterTransaction.with(MainController()).apply {
                pushChangeHandler(FadeChangeHandler())
            })
        }

    }


    private suspend fun initialize(){
        DpConverter.setUp(applicationContext!!)
        DeviceManager.setUp(applicationContext!!)
        VibrateHelper.setUp(applicationContext!!)

        splashVM.initialize()
    }

}

