package com.example.todoplusminus.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoplusminus.TestCoroutineRule
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.IPlannerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlanHistoryContentVM {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var planVM: PlannerViewModel

    @Mock
    lateinit var repository: IPlannerRepository

    private val targetPlanData1 = PlanData.create().apply {
        title = "target1"
    }
    private val targetPlanData2 = PlanData.create().apply {
        title = "target2"
    }
    private val targetPlanData3 = PlanData.create().apply {
        title = "target3"
    }

}