package com.example.todoplusminus.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import java.time.LocalDate
import java.util.*

@Database(entities = [PlannerItemEntity::class, PlannerInfoEntity::class, PlanMemo::class], version = 1)
@TypeConverters(Converters::class)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun userPlanDao() : UserPlanDao
}

@Entity(tableName = "PlannerItem")
data class PlannerItemEntity(
    @PrimaryKey var id : String,
    var title : String,
    var bgColor : Int,
    var index : Int
)

@Entity(tableName = "PlannerInfo",
        foreignKeys = [ForeignKey(
            entity = PlannerItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = CASCADE
        )]
)
data class PlannerInfoEntity(
    @PrimaryKey(autoGenerate = true)var infoId : Int,
    var date : LocalDate,
    var count : Int,
    var planId : String
)

data class PlannerItemInfoEntity(
    @Embedded
    var item : PlannerItemEntity,
    var info : PlannerInfoEntity
)

@Dao
interface UserPlanDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanItem(item : PlannerItemEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanInfo(item : PlannerInfoEntity)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanItem(item : PlannerItemEntity)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanInfo(item : PlannerInfoEntity)
/*    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePlanItemList(itemlist : List<PlannerItemEntity>)
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePlanInfoList(itemlist : List<PlannerInfoEntity>)*/
    @Delete
    suspend fun deletePlanItem(item : PlannerItemEntity)
    @Delete
    suspend fun deletePlanInfo(item : PlannerInfoEntity)

    @Query("delete from PlannerItem where id == :id")
    suspend fun deletePlannerDataById(id : String)

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid order by item.`index` desc")
    fun getAllPlannerData() : LiveData<MutableList<PlanData>>
/*

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid and info.date == :date order by item.`index` desc")
    fun getAllPlannerDataByDate(date : String) : LiveData<MutableList<PlanData>>
*/
    @Query("select * from PlannerItem item left outer join PlannerInfo info on item.id = info.planId where date = :date order by item.`index` desc")
    fun getAllPlannerDataByDate(date : LocalDate) : LiveData<MutableList<PlanData>>


    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planId and info.planId == :id")
    fun getAllPlannerDataById(id : String) : List<PlanData>

    //가장 최신의 인덱스를 가져온다.
    @Query("select `index` from PlannerItem order by `index` DESC LIMIT 1")
    fun getLastIndex() : Int

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid and item.id == :id")
    fun getPlannerDataById(id : String) : PlanData

    @Query("select * from PlannerItem")
    fun getAllPlanItem() : List<PlannerItemEntity>

    @Query("select * from PlannerInfo where date == :date")
    fun getAllPlanInfoByDate(date : LocalDate) : List<PlannerInfoEntity>

    @Query("update PlannerItem set title = :title, bgColor = :bgColor where id = :id")
    fun updateTitleBgById(id : String, title : String, bgColor: Int)

    @Query("select * from PlannerMemo where date = :date")
    fun getMemoByDate(date : LocalDate) : LiveData<PlanMemo>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanMemo(memo : PlanMemo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanMemo(memo : PlanMemo)

}

class Converters{
    @TypeConverter
    fun stringToCalendar(value : String) : LocalDate{
        if(value == "2020/12/03") return LocalDate.parse("2020-12-03")
        Log.d("godgod", "${value}")
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun calendarToString(value : LocalDate) : String{
        return value.toString()
    }
}