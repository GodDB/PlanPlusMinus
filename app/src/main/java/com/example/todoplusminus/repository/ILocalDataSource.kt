package com.example.todoplusminus.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.db.PlannerInfoEntity
import com.example.todoplusminus.db.PlannerItemEntity
import com.example.todoplusminus.db.PlannerItemInfoEntity
import com.example.todoplusminus.entities.PlanData

interface ILocalDataSource {

    fun getPlannerData() : LiveData<MutableList<PlanData>>

    fun getLastIndex() : LiveData<Int>

    suspend fun deletePlannerDataById(id : String)

    suspend fun insertPlannerData(planData: PlanData)

    suspend fun updatePlannerDataList(dataList : List<PlanData>)

    suspend fun updatePlannerData(data : PlanData)

    suspend fun deleteAndUpdateAll(deleteTarget : PlanData, updateTarget : List<PlanData>)

    suspend fun getPlannerDataById(id : String) : PlanData
}

class PlannerMapper() {

    fun planDataToRoomEntity(planData: PlanData) : PlannerItemInfoEntity {
        val item = PlannerItemEntity(planData.id, planData.title, planData.bgColor, planData.index)
        val info = PlannerInfoEntity(planData.infoId, planData.date, planData.count, planData.id)
        return PlannerItemInfoEntity(item, info)
    }

    fun planDataListToRoomEntityList(planDataList : List<PlanData>) : List<PlannerItemInfoEntity>{
        val resultList = mutableListOf<PlannerItemInfoEntity>()
        planDataList.forEach {
            resultList.add(planDataToRoomEntity(it))
        }
        return resultList
    }
}