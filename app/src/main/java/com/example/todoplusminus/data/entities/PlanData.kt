package com.example.todoplusminus.data.entities

import android.media.audiofx.BassBoost
import com.example.todoplusminus.R
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
        const val DEFAULT_BG_COLOR = R.color.dark_gray
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


class BaseID(val id : String){

    companion object{
        private val EMPTY_ID = "none"

        fun randomID() : BaseID{
            return BaseID(UUID.randomUUID().toString())
        }

        fun fromString(str : String) : BaseID{
            return BaseID(str)
        }

        fun createEmpty() : BaseID{
            return BaseID(EMPTY_ID)
        }
    }

    override fun equals(other: Any?): Boolean {
        val otherID = other as? BaseID ?: return false
        return this.id == otherID.id
    }

    override fun toString(): String {
        return id
    }
}