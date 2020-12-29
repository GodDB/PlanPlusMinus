package com.example.todoplusminus.ui.splash

import com.example.todoplusminus.data.repository.SplashRepository
import javax.inject.Inject

class SplashVM @Inject constructor(private val splashRepository: SplashRepository) {

    suspend fun initialize(){
        splashRepository.initialize()
    }
}