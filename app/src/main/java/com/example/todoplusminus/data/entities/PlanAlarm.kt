package com.example.todoplusminus.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.todoplusminus.db.PlannerItemEntity
import java.time.LocalDateTime

@Entity(
    tableName = "PlannerAlarm",
    foreignKeys = [ForeignKey(
        entity = PlannerItemEntity::class,
        parentColumns = ["id"],
        childColumns = ["planId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlanAlarm (
    @PrimaryKey(autoGenerate = true) val alarmId : Int,
    val alarmTitle : String,
    val alarmDateTime : LocalDateTime,
    val alarmType : Int,
    val planId : BaseID
)



