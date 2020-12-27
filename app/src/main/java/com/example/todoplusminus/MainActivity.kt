package com.example.todoplusminus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.controllers.SplashController
import com.example.todoplusminus.databinding.ActivityMainBinding
import com.example.todoplusminus.repository.FontDownloadManager
import com.example.todoplusminus.repository.SharedPrefManager
import com.example.todoplusminus.repository.SplashRepository
import com.example.todoplusminus.vm.SplashVM

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        router = Conductor.attachRouter(this, vb.main, savedInstanceState)

        //todo dagger

        val sharedPrefManager = SharedPrefManager(applicationContext)
        val fontManager = FontDownloadManager(applicationContext)
        val splashRepo = SplashRepository(
            sharedPrefManager,
            fontManager
        )

        val splashVM = SplashVM(splashRepo)

        if (!router.hasRootController())
            router.setRoot(RouterTransaction.with(SplashController(splashVM)))
    }

    override fun onBackPressed() {
        if (!router.handleBack())
            super.onBackPressed()
    }




}