package com.example.todoplusminus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler

import com.example.todoplusminus.base.VBControllerBase
import com.example.todoplusminus.databinding.ControllerSplashBinding
import com.example.todoplusminus.util.ColorManager
import com.example.todoplusminus.util.DeviceManager
import com.example.todoplusminus.util.DpConverter
import com.example.todoplusminus.util.VibrateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashController : VBControllerBase {

    lateinit var binder: ControllerSplashBinding

    constructor() : super()
    constructor(args: Bundle?) : super(args)


    override fun connectViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerSplashBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewBound(v: View) {
        DpConverter.setUp(v.context)
        DeviceManager.setUp(v.context)
        VibrateHelper.setUp(v.context)
        ColorManager.setUp(v.context)

        //todo 완료했을때 적용하자... 지금 테스트 단계이니깐 0.5초 기다리기 좀 그래
/*        CoroutineScope(Dispatchers.Main).launch {
            delay(500)*/

            replaceController(RouterTransaction.with(MainController()).apply {
                pushChangeHandler(FadeChangeHandler())
            })
     /*   }*/

    }

}

