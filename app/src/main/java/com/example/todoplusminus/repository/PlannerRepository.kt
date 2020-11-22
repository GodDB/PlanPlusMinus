package com.example.todoplusminus.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.db.PlannerInfoEntity
import com.example.todoplusminus.db.PlannerItemEntity
import com.example.todoplusminus.db.PlannerItemInfoEntity
import com.example.todoplusminus.entities.PlanData

class PlannerRepository(
    private val localSource: ILocalDataSource
) {


    fun getAllPlannerData(): LiveData<MutableList<PlanData>> = localSource.getPlannerData()

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
}
