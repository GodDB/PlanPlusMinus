package com.example.todoplusminus.data.source.local

import com.example.todoplusminus.db.PlannerInfoEntity
import com.example.todoplusminus.db.PlannerItemEntity
import com.example.todoplusminus.db.PlannerItemInfoEntity
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanMemo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ILocalDataSource {

    fun getAllPlannerData() : Flow<MutableList<PlanData>>

    fun getAllPlanMemo() : Flow<MutableList<PlanMemo>>

    fun getAllPlannerDataByDate(date : LocalDate) : Flow<MutableList<PlanData>>

    fun getAllPlannerDataById(id : String) : Flow<List<PlanData>>

    fun getLastestIndex() : Flow<Int?>

    fun getMemoByDate(date : LocalDate) : Flow<PlanMemo>

    suspend fun deleteMemoByDate(data : LocalDate)

    suspend fun deleteAllData()

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