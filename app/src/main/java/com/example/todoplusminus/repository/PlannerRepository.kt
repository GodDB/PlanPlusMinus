package com.example.todoplusminus.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo

class PlannerRepository(
    private val localSource: ILocalDataSource
) {

    /**
     * initialize function
     * */
    fun getAllPlannerData(): LiveData<MutableList<PlanData>> = localSource.getPlannerData()

    fun getLastIndex() : Int = localSource.getLastIndex()

    fun getMemoByDate(date : String) : LiveData<PlanMemo> = localSource.getMemoByDate(date)

    suspend fun deletePlannerDataById(id: String) {
        localSource.deletePlannerDataById(id)
    }

    suspend fun insertPlannerData(planData: PlanData) {
        localSource.insertPlannerData(planData)
    }

    suspend fun updatePlannerDataList(dataList: List<PlanData>){
        localSource.updatePlannerDataList(dataList)
    }

    suspend fun updatePlannerData(data : PlanData){
        localSource.updatePlannerData(data)
    }

    suspend fun getPlannerDataById(id : String) = localSource.getPlannerDataById(id)

    suspend fun deleteAndUpdateAll(deleteTarget : PlanData, updateTarget : List<PlanData>){
        localSource.deleteAndUpdateAll(deleteTarget, updateTarget)
    }

    suspend fun updateTitleBgById(id : String, title : String, bgColor : Int){
        localSource.updateTitleBgById(id, title, bgColor)
    }

    suspend fun updatePlanMemo(memo : PlanMemo){
        localSource.updatePlanMemo(memo)
    }

    suspend fun insertPlanMemo(memo : PlanMemo){
        localSource.insertPlanMemo(memo)
    }


}
