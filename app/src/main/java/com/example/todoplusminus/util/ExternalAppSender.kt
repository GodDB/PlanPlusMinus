package com.example.todoplusminus.util

import android.content.Context
import android.content.Intent

/**
 * 외부 앱에 데이터를 보내기 위한 Object
 * */
class ExternalAppSender {

    fun sendShareAppMessage(context : Context){
        val sendIntent : Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "test")
            type = "text/plain"
        }

        val sharedIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(sharedIntent)
    }
}