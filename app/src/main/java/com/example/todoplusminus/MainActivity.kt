package com.example.todoplusminus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

       val router = Conductor.attachRouter(this, vb.mainArea, savedInstanceState)

        if(!router.hasRootController())
            router.setRoot(RouterTransaction.with(SplashController()))
    }

}