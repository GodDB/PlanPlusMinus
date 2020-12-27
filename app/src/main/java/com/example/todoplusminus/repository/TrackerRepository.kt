package com.example.todoplusminus.repository

import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackerRepository(private val dataSource: ILocalDataSource) {

    fun getAllDatePlanProject() : Flow<PlanProject> {
        return dataSource.getAllPlannerData().map {
            PlanProject.create(it)
        }
    }
}