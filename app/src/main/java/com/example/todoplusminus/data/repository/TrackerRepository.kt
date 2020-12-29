package com.example.todoplusminus.data.repository

import com.example.todoplusminus.data.entities.PlanProject
import com.example.todoplusminus.data.source.local.ILocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackerRepository(private val dataSource: ILocalDataSource) {

    fun getAllDatePlanProject() : Flow<PlanProject> {
        return dataSource.getAllPlannerData().map {
            PlanProject.create(it)
        }
    }
}