package com.example.todoplusminus.data.source.local

import androidx.room.withTransaction
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanMemo
import com.example.todoplusminus.db.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(val db: PlannerDatabase) :
    ILocalDataSource {

    val mapper = PlannerMapper()

    override fun getAllPlannerData(): Flow<MutableList<PlanData>> =
        db.userPlanDao().getAllPlannerData()

    override fun getAllPlanMemo(): Flow<MutableList<PlanMemo>> =
        db.userPlanDao().getAllPlanMemo()

    override fun getAllPlannerDataByDate(date: LocalDate): Flow<MutableList<PlanData>> =
        db.userPlanDao().getAllPlannerDataByDate(date)

    override fun getAllPlannerDataById(id: BaseID): Flow<List<PlanData>> =
        db.userPlanDao().getAllPlannerDataById(id)

    override fun getLastestIndex(): Flow<Int?> {
        return db.userPlanDao().getLastIndex()
    }

    override fun getMemoByDate(date: LocalDate): Flow<PlanMemo> =
        db.userPlanDao().getMemoByDate(date)

    override fun getAllAlarmData(planId: BaseID): Flow<List<PlannerItemAlarm>> {
        return db.userPlanDao().getAllPlanItemAndAlarm(planId)
    }

    override fun getAlarmData(alarmId: Int): Flow<PlannerItemAlarm> {
        return db.userPlanDao().getPlanItemAndAlarm(alarmId)
    }

    override suspend fun insertAlarmData(alarmData: PlannerAlarmEntity) {
        db.userPlanDao().insertPlanAlarm(alarmData)
    }

    override suspend fun updateAlarmData(alarmData: PlannerAlarmEntity) {
        db.userPlanDao().updatePlanAlarm(alarmData)
    }

    override suspend fun deleteAlarmDataById(alarmId: Int) {
        db.userPlanDao().deleteAlarmById(alarmId)
    }


    override suspend fun deleteMemoByDate(data: LocalDate) {
        db.userPlanDao().deletePlanMemoByData(data)
    }

    override suspend fun deleteAllData() {
        db.withTransaction {
            db.userPlanDao().deleteAllPlanItem()
            db.userPlanDao().deleteAllPlanMemo()
        }
    }


    override suspend fun deletePlannerDataById(id: BaseID) {
        db.userPlanDao().deletePlannerDataById(id)
    }

    override suspend fun insertPlannerData(planData: PlanData) {
        val itemAndInfo = mapper.planDataToRoomEntity(planData)

        db.withTransaction {
            db.userPlanDao().insertPlanItem(itemAndInfo.item)
            db.userPlanDao().insertPlanInfo(itemAndInfo.info)
        }
    }

    override suspend fun updatePlannerDataList(dataList: List<PlanData>) {
        val itemAndInfoList = mapper.planDataListToRoomEntityList(dataList)

        db.withTransaction {
            itemAndInfoList.forEach {
                db.userPlanDao().updatePlanItem(it.item)
                db.userPlanDao().updatePlanInfo(it.info)
            }
        }
    }

    override suspend fun updatePlannerData(data: PlanData) {
        val itemAndInfo = mapper.planDataToRoomEntity(data)

        db.withTransaction {
            db.userPlanDao().updatePlanItem(itemAndInfo.item)
            db.userPlanDao().updatePlanInfo(itemAndInfo.info)
        }
    }

    override suspend fun deleteAndUpdateAll(deleteTarget: PlanData, updateTarget: List<PlanData>) {
        val deleteItemAndInfo = mapper.planDataToRoomEntity(deleteTarget)
        val updateItemAndInfoList = mapper.planDataListToRoomEntityList(updateTarget)

        db.withTransaction {

            updateItemAndInfoList.forEach {
                db.userPlanDao().updatePlanItem(it.item)
                db.userPlanDao().updatePlanInfo(it.info)
            }

            db.userPlanDao().deletePlanInfo(deleteItemAndInfo.info)
            db.userPlanDao().deletePlanItem(deleteItemAndInfo.item)
        }
    }

    override suspend fun getPlannerDataById(id: BaseID): PlanData =
        db.userPlanDao().getPlannerDataById(id)

    override suspend fun getAllPlanItem(): List<PlannerItemEntity> =
        db.userPlanDao().getAllPlanItem()

    override suspend fun getAllPlanInfoByDate(date: LocalDate): List<PlannerInfoEntity> =
        db.userPlanDao().getAllPlanInfoByDate(date)

    override suspend fun updateTitleBgById(id: BaseID, title: String, bgColor: Int) {
        db.userPlanDao().updateTitleBgById(id, title, bgColor)
    }

    override suspend fun updatePlanMemo(memo: PlanMemo) {
        db.userPlanDao().updatePlanMemo(memo)
    }

    override suspend fun insertPlanMemo(memo: PlanMemo) {
        db.userPlanDao().insertPlanMemo(memo)
    }

    override suspend fun insertPlanInfo(info: PlannerInfoEntity) {
        db.userPlanDao().insertPlanInfo(info)
    }


}


