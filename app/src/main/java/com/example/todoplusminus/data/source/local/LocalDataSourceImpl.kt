package com.example.todoplusminus.data.source.local

import android.util.Log
import androidx.room.withTransaction
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanMemo
import com.example.todoplusminus.db.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(val db: PlannerDatabase) :
    ILocalDataSource {

    private val dao : UserPlanDao = db.userPlanDao()

    val mapper = PlannerMapper()

    override fun getAllPlannerData(): Flow<MutableList<PlanData>> =
        dao.getAllPlannerData()

    override fun getAllPlanMemo(): Flow<MutableList<PlanMemo>> =
        dao.getAllPlanMemo()

    override fun getAllPlannerDataByDate(date: LocalDate): Flow<MutableList<PlanData>> =
        dao.getAllPlannerDataByDate(date)

    override fun getAllPlannerDataById(id: BaseID): Flow<List<PlanData>> =
        dao.getAllPlannerDataById(id)

    override fun getLastestIndex(): Flow<Int?> {
        return dao.getLastIndex()
    }

    override fun getMemoByDate(date: LocalDate): Flow<PlanMemo> =
        dao.getMemoByDate(date)

    override fun getAllAlarmData(): Flow<List<PlannerItemAlarm>> {
        return dao.getAllAlarmData()
    }

    override fun getAlarmData(alarmId: Int): Flow<PlannerItemAlarm> {
        return dao.getPlanItemAndAlarm(alarmId)
    }

    override fun getAlarmDataListByPlanId(planId: BaseID): Flow<List<PlannerItemAlarm>> {
        return dao.getAlarmDataListByPlanId(planId)
    }

    override fun getLatestAlarmId(): Flow<Int?> {
        return dao.getLatestAlarmId()
    }

    override fun getPlannerTitle(planId: BaseID): Flow<String> {
        return dao.getPlanItemTitle(planId)
    }

    override suspend fun insertAlarmData(alarmData: PlannerAlarmEntity) {
        dao.insertPlanAlarm(alarmData)
    }

    override suspend fun updateAlarmData(alarmData: PlannerAlarmEntity) {
        dao.updatePlanAlarm(alarmData)
    }

    override suspend fun deleteAlarmDataById(alarmId: Int) {
        dao.deleteAlarmById(alarmId)
    }


    override suspend fun deleteMemoByDate(data: LocalDate) {
        dao.deletePlanMemoByData(data)
    }

    override suspend fun deleteAllData() {
        db.withTransaction {
            dao.deleteAllPlanItem()
            dao.deleteAllPlanMemo()
        }
    }


    override suspend fun deletePlannerDataById(id: BaseID) {
        dao.deletePlanItemById(id)
    }

    override suspend fun insertPlannerData(planData: PlanData) {
        val itemAndInfo = mapper.planDataToRoomEntity(planData)

        db.withTransaction {
            dao.insertPlanItem(itemAndInfo.item)
            dao.insertPlanInfo(itemAndInfo.info)
        }
    }

    override suspend fun updatePlannerDataList(dataList: List<PlanData>) {
        val itemAndInfoList = mapper.planDataListToRoomEntityList(dataList)

        db.withTransaction {
            itemAndInfoList.forEach {
                dao.updatePlanItem(it.item)
                dao.updatePlanInfo(it.info)
            }
        }
    }

    override suspend fun updatePlannerData(data: PlanData) {
        val itemAndInfo = mapper.planDataToRoomEntity(data)

        db.withTransaction {
            dao.updatePlanItem(itemAndInfo.item)
            dao.updatePlanInfo(itemAndInfo.info)
        }
    }

    override suspend fun deleteAndUpdateAll(deleteTarget: PlanData, updateTarget: List<PlanData>) {
        val deleteItemAndInfo = mapper.planDataToRoomEntity(deleteTarget)
        val updateItemAndInfoList = mapper.planDataListToRoomEntityList(updateTarget)

        db.withTransaction {

            updateItemAndInfoList.forEach {
                dao.updatePlanItem(it.item)
                dao.updatePlanInfo(it.info)
            }

            dao.deletePlanInfo(deleteItemAndInfo.info)
            dao.deletePlanItem(deleteItemAndInfo.item)
        }
    }

    override suspend fun getPlannerDataById(id: BaseID): PlanData =
        dao.getPlannerDataById(id)

    override suspend fun getAllPlanItem(): List<PlannerItemEntity> =
        dao.getAllPlanItem()

    override suspend fun getAllPlanInfoByDate(date: LocalDate): List<PlannerInfoEntity> =
        dao.getAllPlanInfoByDate(date)

    override suspend fun updateTitleBgById(id: BaseID, title: String, bgColor: Int) {
        dao.updatePlanItemTitleBgById(id, title, bgColor)
    }

    override suspend fun updatePlanMemo(memo: PlanMemo) {
        dao.updatePlanMemo(memo)
    }

    override suspend fun insertPlanMemo(memo: PlanMemo) {
        dao.insertPlanMemo(memo)
    }

    override suspend fun insertPlanInfo(info: PlannerInfoEntity) {
        dao.insertPlanInfo(info)
    }



}


