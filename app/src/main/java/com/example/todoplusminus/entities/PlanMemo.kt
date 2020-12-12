package com.example.todoplusminus.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoplusminus.util.DateHelper
import java.time.LocalDate

@Entity(tableName = "PlannerMemo")
data class PlanMemo(
    @PrimaryKey var date: LocalDate,   //하루에 한개의 메모만 저장 가능하므로, date를 key값으로
    var contents: String
) {
    companion object {
        fun create() = PlanMemo(
            DateHelper.getCurDate(),
            ""
        )
    }
}
