package com.example.todoplusminus.entities

import android.graphics.Color
import com.example.todoplusminus.util.DateHelper
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
                DateHelper.getCurDate(),
                0,
                0
            )
        }

    }


    fun increaseCount(count: Int) {
        if (this.count + count >= 0)
            this.count += count
    }

    fun copy(): PlanData = PlanData(id, index, title, bgColor, date, count, infoId)

    override fun toString(): String {
        return "id : $id, " +
                "index : $index, " +
                "bgColor : $bgColor, " +
                "date : $date ," +
                "count : $count, " +
                "infoId : $infoId"
    }

}