package com.example.todoplusminus.db

import android.content.Context
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanAlarm
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanMemo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime


@Database(
    entities = [PlannerItemEntity::class, PlannerInfoEntity::class, PlanMemo::class, PlanAlarm::class],
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

@Dao
interface UserPlanDao {

    //planInfo는 planItem의 fk관계여서 planItem삭제시 planInfo도 삭제된다.
    @Query("delete from planneritem")
    suspend fun deleteAllPlanItem()

    @Query("delete from PlannerMemo")
    suspend fun deleteAllPlanMemo()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanItem(item: PlannerItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanInfo(item: PlannerInfoEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanItem(item: PlannerItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanInfo(item: PlannerInfoEntity)

    @Delete
    suspend fun deletePlanItem(item: PlannerItemEntity)

    @Query("delete from PlannerMemo where date == :date")
    suspend fun deletePlanMemoByData(date: LocalDate)

    @Delete
    suspend fun deletePlanInfo(item: PlannerInfoEntity)

    @Query("delete from PlannerItem where id == :id")
    suspend fun deletePlannerDataById(id: BaseID)

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid order by item.`index` desc")
    fun getAllPlannerData(): Flow<MutableList<PlanData>>


    @Query("select * from PlannerItem item left outer join PlannerInfo info on item.id = info.planId where date = :date order by item.`index` desc")
    fun getAllPlannerDataByDate(date: LocalDate): Flow<MutableList<PlanData>>


    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planId and info.planId == :id")
    fun getAllPlannerDataById(id: BaseID): Flow<List<PlanData>>

    //가장 최신의 인덱스를 가져온다.
    @Query("select `index` from PlannerItem order by `index` DESC LIMIT 1")
    fun getLastIndex(): Flow<Int?>

    @Query("select * from PlannerItem item, PlannerInfo info where item.id == info.planid and item.id == :id")
    fun getPlannerDataById(id: BaseID): PlanData

    @Query("select * from PlannerItem")
    fun getAllPlanItem(): List<PlannerItemEntity>

    @Query("select * from PlannerInfo where date == :date")
    fun getAllPlanInfoByDate(date: LocalDate): List<PlannerInfoEntity>

    @Query("update PlannerItem set title = :title, bgColor = :bgColor where id = :id")
    fun updateTitleBgById(id: BaseID, title: String, bgColor: Int)

    @Query("select * from PlannerMemo")
    fun getAllPlanMemo(): Flow<MutableList<PlanMemo>>

    @Query("select * from PlannerMemo where date = :date")
    fun getMemoByDate(date: LocalDate): Flow<PlanMemo>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlanMemo(memo: PlanMemo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanMemo(memo: PlanMemo)

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
    fun stringToLocalDateTime(value : String) : LocalDateTime{
        return LocalDateTime.parse(value)
    }

    @TypeConverter
    fun localDateTimeToString(value : LocalDateTime) : String{
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