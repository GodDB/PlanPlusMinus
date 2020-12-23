package com.example.todoplusminus.vm

import com.example.todoplusminus.repository.SettingRepository

class SplashVM(private val splashRepository: SplashRepository) {

    fun initialize(){
        splashRepository.initialize()
    }
}