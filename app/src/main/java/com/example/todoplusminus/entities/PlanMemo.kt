package com.example.todoplusminus.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoplusminus.util.TimeProvider

@Entity(tableName = "PlannerMemo")
data class PlanMemo(
    @PrimaryKey var date: String,   //하루에 한개의 메모만 저장 가능하므로, date를 key값으로
    var contents: String
) {
    companion object {
        fun create() = PlanMemo(TimeProvider.getCurDate(),
            "")
    }
}
