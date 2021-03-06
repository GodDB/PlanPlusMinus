package com.example.todoplusminus.data.source.local.db

import android.content.Context
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanMemo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime


@Database(
    entities = [PlannerItemEntity::class, PlannerInfoEntity::class, PlanMemo::class, PlannerAlarmEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun userPlanDao(): UserPlanDao

    companion object {
        private var INSTANCE: PlannerDatabase? = null
        fun getInstance(context: Context): PlannerDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    PlannerDatabase::class.java,
                    "PlannerDB.sqlite3"
                )
                    .build()
            }
            return INSTANCE!!
        }
    }
}

@Entity(tableName = "PlannerItem")
data class PlannerItemEntity(
    @PrimaryKey var id: BaseID,
    var title: String,
    var bgColor: Int,
    var index: Int
)

@Entity(
    tableName = "PlannerInfo",
    foreignKeys = [ForeignKey(
        entity = PlannerItemEntity::class,
        parentColumns = ["id"],
        childColumns = ["planId"],
        onDelete = CASCADE
    )]
)
data class PlannerInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var infoId: Int,
    var date: LocalDate,
    var count: Int,
    var planId: BaseID
)


data class PlannerItemInfoEntity(
    @Embedded
    var item: PlannerItemEntity,
    var info: PlannerInfoEntity
)


@Entity(
    tableName = "PlannerAlarm",
    foreignKeys = [ForeignKey(
        entity = PlannerItemEntity::class,
        parentColumns = ["id"],
        childColumns = ["planId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlannerAlarmEntity (
    @PrimaryKey val alarmId : Int,
    val alarmTime : LocalTime,
    val alarmMonday : Boolean,
    val alarmTuesday : Boolean,
    val alarmWednesday : Boolean,
    val alarmThursday : Boolean,
    val alarmFriday : Boolean,
    val alarmSaturday : Boolean,
    val alarmSunday : Boolean,

    val planId : BaseID //fk
)

data class PlannerItemAlarm(
    @Embedded
    var planItem : PlannerItemEntity,
    @Embedded
    var planAlarm : PlannerAlarmEntity
)

@Dao
interface UserPlanDao {

    //plan item
    @Query("delete from planneritem")
    suspend fun deleteAllPlanItem()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanItem(item: PlannerItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanItem(item: PlannerItemEntity)

    @Delete
    suspend fun deletePlanItem(item: PlannerItemEntity)

    @Query("select * from PlannerItem")
    fun getAllPlanItem(): List<PlannerItemEntity>

    @Query("delete from PlannerItem where id == :id")
    suspend fun deletePlanItemById(id: BaseID)

    @Query("select `index` from PlannerItem order by `index` DESC LIMIT 1")
    fun getLastIndex(): Flow<Int?>

    @Query("select title from PlannerItem where id == :planId")
    fun getPlanItemTitle(planId : BaseID) : Flow<String>

    @Query("update PlannerItem set title = :title, bgColor = :bgColor where id = :id")
    fun updatePlanItemTitleBgById(id: BaseID, title: String, bgColor: Int)

    //plan info
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanInfo(item: PlannerInfoEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanInfo(item: PlannerInfoEntity)

    @Query("delete from PlannerMemo")
    suspend fun deleteAllPlanMemo()

    @Delete
    suspend fun deletePlanInfo(item: PlannerInfoEntity)

    @Query("select * from PlannerInfo where date == :date")
    fun getAllPlanInfoByDate(date: LocalDate): List<PlannerInfoEntity>


    // memo
    @Query("delete from PlannerMemo where date == :date")
    suspend fun deletePlanMemoByData(date: LocalDate)

    @Query("select * from PlannerMemo")
    fun getAllPlanMemo(): Flow<MutableList<PlanMemo>>

    @Query("select * from PlannerMemo where date = :date")
    fun getMemoByDate(date: LocalDate): Flow<PlanMemo>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanMemo(memo: PlanMemo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanMemo(memo: PlanMemo)


    // plan data (plan info + plan item)
    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid order by item.`index` desc")
    fun getAllPlannerData(): Flow<MutableList<PlanData>>

    @Query("select * from PlannerItem item left outer join PlannerInfo info on item.id = info.planId where date = :date order by item.`index` desc")
    fun getAllPlannerDataByDate(date: LocalDate): Flow<MutableList<PlanData>>

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planId and info.planId == :id")
    fun getAllPlannerDataById(id: BaseID): Flow<List<PlanData>>

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid and item.id == :id")
    fun getPlannerDataById(id: BaseID): PlanData


    //alarm item

    //가장 최신의 알람Id를 가져온다
    @Query("select `alarmId` from PlannerAlarm order by `alarmId` DESC LIMIT 1")
    fun getLatestAlarmId(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlanAlarm(alarmEntity : PlannerAlarmEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlanAlarm(alarmEntity: PlannerAlarmEntity)

    @Query("delete from PlannerAlarm where alarmId == :alarmId")
    fun deleteAlarmById(alarmId: Int)

    @Query("delete from PlannerAlarm")
    fun deleteAllAlarm()

    //alarm data (plan item + alarm item)

    @Query("select * from PlannerItem item, PlannerAlarm alarm where item.id == alarm.planId")
    fun getAllAlarmData() : Flow<List<PlannerItemAlarm>>

    @Query("select * from PlannerItem item, PlannerAlarm alarm where item.id == alarm.planId and item.id == :planId")
    fun getAlarmDataListByPlanId(planId : BaseID) : Flow<List<PlannerItemAlarm>>

    @Query("select * from PlannerItem item, PlannerAlarm alarm where item.id == alarm.planId and alarm.alarmId == :alarmId")
    fun getPlanItemAndAlarm(alarmId : Int) : Flow<PlannerItemAlarm>


}

class Converters {
    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun localDateToString(value: LocalDate): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalTime(value : String) : LocalTime {
        return LocalTime.parse(value)
    }

    @TypeConverter
    fun localTimeToString(value : LocalTime) : String{
        return value.toString()
    }

    @TypeConverter
    fun stringToBaseId(value: String): BaseID {
        return BaseID.fromString(value)
    }

    @TypeConverter
    fun baseIdToString(value: BaseID): String {
        return value.toString()
    }
}