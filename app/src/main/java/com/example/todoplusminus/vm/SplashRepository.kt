package com.example.todoplusminus.vm

import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.repository.FontDownloadManager
import com.example.todoplusminus.repository.SharedPrefManager

class SplashRepository(
    private val prefManager: SharedPrefManager,
    private val fontDownloadManager: FontDownloadManager
) {

    suspend fun initialize() {
        populateAppConfig()
        downloadFont(AppConfig.fontName)
    }

    private fun populateAppConfig(){
        prefManager.populateAppConfig()
    }

    private suspend fun downloadFont(fontName : String){
        fontDownloadManager.downloadFont(fontName)
    }
}