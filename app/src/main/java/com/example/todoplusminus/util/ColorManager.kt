package com.example.todoplusminus.util

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.todoplusminus.R

/**
 * 앱내에서 제공하는 컬러값들을 지닌 매니저 object
 * */
object ColorManager {

    fun getRandomColor() = colorList[(colorList.indices).random()]

    val colorList = listOf<Int>(
            R.color.pastel_color1,
            R.color.pastel_color2,
            R.color.pastel_color3,
            R.color.pastel_color4,
            R.color.pastel_color5,
            R.color.pastel_color6,
            R.color.pastel_color7,
            R.color.pastel_color8,
            R.color.pastel_color9,
            R.color.pastel_color10,
            R.color.pastel_color11,
            R.color.pastel_color12,
            R.color.pastel_color13,
            R.color.pastel_color14,
            R.color.pastel_color15,
            R.color.pastel_color16,
            R.color.pastel_color17,
            R.color.pastel_color18,
            R.color.pastel_color19,
            R.color.pastel_color20,
            R.color.pastel_color21,
            R.color.pastel_color22,
            R.color.pastel_color23,
            R.color.pastel_color24,
            R.color.pastel_color25,
            R.color.pastel_color26,
            R.color.pastel_color27,
            R.color.pastel_color28,
            R.color.pastel_color29,
            R.color.pastel_color30,
            R.color.pastel_color31,
            R.color.pastel_color32,
            R.color.pastel_color33,
            R.color.pastel_color34,
            R.color.pastel_color35,
            R.color.pastel_color36,
            R.color.pastel_color37,
            R.color.pastel_color38,
            R.color.pastel_color39,
            R.color.pastel_color40,
            R.color.pastel_color41,
            R.color.pastel_color42,
            R.color.pastel_color43,
            R.color.pastel_color44,
            R.color.pastel_color45,
            R.color.pastel_color46,
            R.color.pastel_color47,
            R.color.pastel_color48,
            R.color.pastel_color49,
            R.color.pastel_color50,
            R.color.pastel_color51,
            R.color.pastel_color52,
            R.color.pastel_color53,
            R.color.pastel_color54,
            R.color.pastel_color55
        )

}