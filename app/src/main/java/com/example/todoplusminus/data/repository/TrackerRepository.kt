package com.example.todoplusminus.data.repository

import com.example.todoplusminus.data.entities.PlanProject
import com.example.todoplusminus.data.source.local.ILocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrackerRepository @Inject constructor(private val dataSource: ILocalDataSource) : ITrackerRepository {

    override fun getAllDatePlanProject(): Flow<PlanProject> {
        return dataSource.getAllPlannerData().map {
            PlanProject.create(it)
        }
    }
}