package com.example.todoplusminus.entities

import android.graphics.Color
import com.example.todoplusminus.util.TimeHelper
import java.time.LocalDate
import java.util.*

class PlanData(
    var id: String,
    var index: Int,
    var title: String,
    var bgColor: Int,
    var date: LocalDate,
    var count: Int,
    var infoId: Int
) {

    companion object {
        const val DEFAULT_BG_COLOR = Color.GRAY
        const val EMPTY_ID = "none"

        fun create(): PlanData {
            return PlanData(
                UUID.randomUUID().toString(),
                0,
                "",
                DEFAULT_BG_COLOR,
                TimeHelper.getCurDate(),
                0,
                0
            )
        }
    }


    fun incrementCount(count: Int) {
        if (this.count + count >= 0)
            this.count += count
    }


}