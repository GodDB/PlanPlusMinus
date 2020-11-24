package com.example.todoplusminus.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.example.todoplusminus.db.PlannerDatabase
import com.example.todoplusminus.db.PlannerItemInfoEntity
import com.example.todoplusminus.entities.PlanData

class LocalDataSourceImpl(val db: PlannerDatabase) : ILocalDataSource {

    val mapper = PlannerMapper()

    override fun getPlannerData(): LiveData<MutableList<PlanData>> =
        db.userPlanDao().getAllPlannerData()


    override fun getLastIndex(): LiveData<Int> =
        db.userPlanDao().getLastIndex()


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

}