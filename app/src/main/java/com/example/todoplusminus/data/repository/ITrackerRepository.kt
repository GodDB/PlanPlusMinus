package com.example.todoplusminus.data.repository

import com.example.todoplusminus.data.entities.PlanProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ITrackerRepository {

    fun getAllDatePlanProject(): Flow<PlanProject>
}