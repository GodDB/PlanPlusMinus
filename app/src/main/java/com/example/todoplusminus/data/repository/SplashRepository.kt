package com.example.todoplusminus.data.repository

import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import javax.inject.Inject

class SplashRepository @Inject constructor(
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