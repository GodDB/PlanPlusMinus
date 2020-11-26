package com.example.todoplusminus.util

import android.content.Context

object TitleManager {

    private var mContext : Context? = null

    fun setUp(context : Context){
        mContext = context
    }

    val titleList = listOf<String>(
        "갓갓갓",
        "낫낫낫",
        "닷닷닷"
    )
}