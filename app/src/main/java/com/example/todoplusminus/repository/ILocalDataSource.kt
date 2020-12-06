package com.example.todoplusminus.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoplusminus.db.PlannerInfoEntity
import com.example.todoplusminus.db.PlannerItemEntity
import com.example.todoplusminus.db.PlannerItemInfoEntity
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import java.time.LocalDate

interface ILocalDataSource {

    fun getAllPlannerData() : LiveData<MutableList<PlanData>>

    fun getAllPlannerDataByDate(date : LocalDate) : LiveData<MutableList<PlanData>>

    fun getAllPlannerDataById(id : String) : List<PlanData>

    fun getLastIndex() : Int

    fun getMemoByDate(date : LocalDate) : LiveData<PlanMemo>

    suspend fun deletePlannerDataById(id : String)

    suspend fun insertPlannerData(planData: PlanData)

    suspend fun updatePlannerDataList(dataList : List<PlanData>)

    suspend fun updatePlannerData(data : PlanData)

    suspend fun deleteAndUpdateAll(deleteTarget : PlanData, updateTarget : List<PlanData>)

    suspend fun getPlannerDataById(id : String) : PlanData

    suspend fun getAllPlanItem() : List<PlannerItemEntity>

    suspend fun getAllPlanInfoByDate(date : LocalDate) : List<PlannerInfoEntity>

    suspend fun updateTitleBgById(id : String, title : String, bgColor : Int)

    suspend fun updatePlanMemo(memo : PlanMemo)

    suspend fun insertPlanMemo(memo : PlanMemo)

    suspend fun insertPlanInfo(info : PlannerInfoEntity)
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