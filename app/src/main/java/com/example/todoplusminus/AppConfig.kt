package com.example.todoplusminus

import android.graphics.Typeface
import com.example.todoplusminus.data.source.remote.FontDownloadManager

object AppConfig {

    const val OFFICIAL_SITE : String = ""

    const val version : String = "1.0.0"
    var font : Typeface? = null
    var fontName : String = FontDownloadManager.FONT_YEON_SUNG
    var showSuggestedKeyword : Boolean = true
    var swipeDirectionToRight : Boolean = true
    var showCalendar : Boolean = false
    var enableAlarm : Boolean = true
    var planSize : Int = 15
}