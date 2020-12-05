package com.example.todoplusminus.repository

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.example.todoplusminus.db.PlannerDatabase
import com.example.todoplusminus.db.PlannerInfoEntity
import com.example.todoplusminus.db.PlannerItemEntity
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import java.time.LocalDate

class LocalDataSourceImpl(val db: PlannerDatabase) : ILocalDataSource {

    val mapper = PlannerMapper()

    override fun getAllPlannerData(): LiveData<MutableList<PlanData>> =
        db.userPlanDao().getAllPlannerData()

    override fun getAllPlannerDataByDate(date : LocalDate): LiveData<MutableList<PlanData>> =
        db.userPlanDao().getAllPlannerDataByDate(date)

    override fun getAllPlannerDataById(id: String): LiveData<MutableList<PlanData>> =
        db.userPlanDao().getAllPlannerDataById(id)

    override fun getLastIndex(): Int =
        db.userPlanDao().getLastIndex()

    override fun getMemoByDate(date: LocalDate): LiveData<PlanMemo> = db.userPlanDao().getMemoByDate(date)


    override suspend fun deletePlannerDataById(id: String) {
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

    override suspend fun getPlannerDataById(id: String): PlanData =
        db.userPlanDao().getPlannerDataById(id)

    override suspend fun getAllPlanItem(): List<PlannerItemEntity> =
        db.userPlanDao().getAllPlanItem()

    override suspend fun getAllPlanInfoByDate(date: LocalDate): List<PlannerInfoEntity> =
        db.userPlanDao().getAllPlanInfoByDate(date)

    override suspend fun updateTitleBgById(id: String, title: String, bgColor: Int) {
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