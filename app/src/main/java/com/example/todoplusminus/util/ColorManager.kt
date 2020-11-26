package com.example.todoplusminus.util

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.todoplusminus.R

/**
 * 앱내에서 제공하는 컬러값들을 지닌 매니저 object
 * */
object ColorManager {
    private var mContext: Context? = null

    fun setUp(context: Context) {
        mContext = context
    }

    fun getRandomColor() = colorList[(colorList.indices).random()]

    val colorList
        get() = if (mContext != null) listOf<Int>(
            ContextCompat.getColor(mContext!!, R.color.pastel_color1),
            ContextCompat.getColor(mContext!!, R.color.pastel_color2),
            ContextCompat.getColor(mContext!!, R.color.pastel_color3),
            ContextCompat.getColor(mContext!!, R.color.pastel_color4),
            ContextCompat.getColor(mContext!!, R.color.pastel_color5),
            ContextCompat.getColor(mContext!!, R.color.pastel_color6),
            ContextCompat.getColor(mContext!!, R.color.pastel_color7),
            ContextCompat.getColor(mContext!!, R.color.pastel_color8),
            ContextCompat.getColor(mContext!!, R.color.pastel_color9),
            ContextCompat.getColor(mContext!!, R.color.pastel_color10),
            ContextCompat.getColor(mContext!!, R.color.pastel_color11),
            ContextCompat.getColor(mContext!!, R.color.pastel_color12),
            ContextCompat.getColor(mContext!!, R.color.pastel_color13),
            ContextCompat.getColor(mContext!!, R.color.pastel_color14),
            ContextCompat.getColor(mContext!!, R.color.pastel_color15),
            ContextCompat.getColor(mContext!!, R.color.pastel_color16),
            ContextCompat.getColor(mContext!!, R.color.pastel_color17),
            ContextCompat.getColor(mContext!!, R.color.pastel_color18),
            ContextCompat.getColor(mContext!!, R.color.pastel_color19),
            ContextCompat.getColor(mContext!!, R.color.pastel_color20),
            ContextCompat.getColor(mContext!!, R.color.pastel_color21),
            ContextCompat.getColor(mContext!!, R.color.pastel_color22),
            ContextCompat.getColor(mContext!!, R.color.pastel_color23),
            ContextCompat.getColor(mContext!!, R.color.pastel_color24),
            ContextCompat.getColor(mContext!!, R.color.pastel_color25),
            ContextCompat.getColor(mContext!!, R.color.pastel_color26),
            ContextCompat.getColor(mContext!!, R.color.pastel_color27),
            ContextCompat.getColor(mContext!!, R.color.pastel_color28),
            ContextCompat.getColor(mContext!!, R.color.pastel_color29),
            ContextCompat.getColor(mContext!!, R.color.pastel_color30),
            ContextCompat.getColor(mContext!!, R.color.pastel_color31),
            ContextCompat.getColor(mContext!!, R.color.pastel_color32),
            ContextCompat.getColor(mContext!!, R.color.pastel_color33),
            ContextCompat.getColor(mContext!!, R.color.pastel_color34),
            ContextCompat.getColor(mContext!!, R.color.pastel_color35),
            ContextCompat.getColor(mContext!!, R.color.pastel_color36),
            ContextCompat.getColor(mContext!!, R.color.pastel_color37),
            ContextCompat.getColor(mContext!!, R.color.pastel_color38),
            ContextCompat.getColor(mContext!!, R.color.pastel_color39),
            ContextCompat.getColor(mContext!!, R.color.pastel_color40),
            ContextCompat.getColor(mContext!!, R.color.pastel_color41),
            ContextCompat.getColor(mContext!!, R.color.pastel_color42),
            ContextCompat.getColor(mContext!!, R.color.pastel_color43),
            ContextCompat.getColor(mContext!!, R.color.pastel_color44),
            ContextCompat.getColor(mContext!!, R.color.pastel_color45),
            ContextCompat.getColor(mContext!!, R.color.pastel_color46),
            ContextCompat.getColor(mContext!!, R.color.pastel_color47),
            ContextCompat.getColor(mContext!!, R.color.pastel_color48),
            ContextCompat.getColor(mContext!!, R.color.pastel_color49),
            ContextCompat.getColor(mContext!!, R.color.pastel_color50),
            ContextCompat.getColor(mContext!!, R.color.pastel_color51),
            ContextCompat.getColor(mContext!!, R.color.pastel_color52),
            ContextCompat.getColor(mContext!!, R.color.pastel_color53),
            ContextCompat.getColor(mContext!!, R.color.pastel_color54),
            ContextCompat.getColor(mContext!!, R.color.pastel_color55)
        ) else listOf()

}