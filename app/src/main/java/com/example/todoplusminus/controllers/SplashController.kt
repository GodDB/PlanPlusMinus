package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.MainController

import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.databinding.ControllerSplashBinding
import com.example.todoplusminus.util.*
import com.example.todoplusminus.vm.SplashVM
import kotlinx.coroutines.*

class SplashController : VBControllerBase {

    private lateinit var binder: ControllerSplashBinding
    private lateinit var splashVM: SplashVM

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(splashVM : SplashVM){
        this.splashVM = splashVM
    }

    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerSplashBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        DpConverter.setUp(v.context)
        DeviceManager.setUp(v.context)
        VibrateHelper.setUp(v.context)

        //todo 완료했을때 적용하자... 지금 테스트 단계이니깐 0.5초 기다리기 좀 그래
        CoroutineScope(Dispatchers.Main).launch {
            initialize()

         /*   delay(500)*/
            Log.d("godgod", "${AppConfig.font}")
            replaceController(RouterTransaction.with(MainController()).apply {
                pushChangeHandler(FadeChangeHandler())
            })
        }

    }

    private suspend fun initialize(){
        withContext(Dispatchers.Main){
            Log.d("godgod", "start initialize")
            splashVM.initialize()
            Log.d("godgod", "${AppConfig.font}")
        }
    }

}

