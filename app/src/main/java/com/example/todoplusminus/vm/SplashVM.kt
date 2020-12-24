package com.example.todoplusminus.vm

import com.example.todoplusminus.repository.SettingRepository

class SplashVM(private val splashRepository: SplashRepository) {

    suspend fun initialize(){
        splashRepository.initialize()
    }
}