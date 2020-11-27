package com.example.todoplusminus.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.todoplusminus.entities.PlanData

@Database(entities = [PlannerItemEntity::class, PlannerInfoEntity::class], version = 1)
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
    var date : String,
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

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid and info.date == :date")
    suspend fun getPlannerDataByDate(date : String) : List<PlanData>

    @Query("select MAX(`index`) as Int from PlannerItem")
    fun getLastIndex() : LiveData<Int>

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid and item.id == :id")
    fun getPlannerDataById(id : String) : PlanData
}
