package com.example.todoplusminus.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoplusminus.PlanProjectTest
import com.example.todoplusminus.TestCoroutineRule
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.TrackerRepository
import com.example.todoplusminus.util.LocalDateRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TestTrackerVM {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var trackerVM: TrackerVM
/*
    @Mock
    lateinit var trackerRepository: TrackerRepository*/

    lateinit var testPlanProject : PlanProject
    lateinit var testLocalDateRange : LocalDateRange
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)

        val planData = mutableListOf<PlanData>().apply{
            add(PlanData.create().apply { title = "갓갓"; count = 1; date = LocalDate.of(2020,12,1) })
            add(PlanData.create().apply { title = "갓갓"; count = 2; date = LocalDate.of(2020,12,2) })
            add(PlanData.create().apply { title = "갓갓"; count = 3; date = LocalDate.of(2020,12,3) })
            add(PlanData.create().apply { title = "갓갓"; count = 4; date = LocalDate.of(2020,12,4) })
            add(PlanData.create().apply { title = "갓갓"; count = 5; date = LocalDate.of(2020,12,5) })
            add(PlanData.create().apply { title = "갓갓"; count = 6; date = LocalDate.of(2020,12,6) })
            add(PlanData.create().apply { title = "갓갓"; count = 7; date = LocalDate.of(2020,12,7) })
            add(PlanData.create().apply { title = "갓갓"; count = 8; date = LocalDate.of(2020,12,8) })
            add(PlanData.create().apply { title = "갓갓"; count = 9; date = LocalDate.of(2020,12,9) })
            add(PlanData.create().apply { title = "갓갓"; count = 10; date = LocalDate.of(2020,12,10)})
            add(PlanData.create().apply { title = "갓갓"; count = 11; date = LocalDate.of(2020,12,11)})
            add(PlanData.create().apply { title = "갓갓"; count = 12; date = LocalDate.of(2020,12,12)})
            add(PlanData.create().apply { title = "갓갓"; count = 13; date = LocalDate.of(2020,12,13)})
            add(PlanData.create().apply { title = "갓갓"; count = 14; date = LocalDate.of(2020,12,14)})
            add(PlanData.create().apply { title = "갓갓"; count = 15; date = LocalDate.of(2020,12,15)})
            add(PlanData.create().apply { title = "갓갓"; count = 16; date = LocalDate.of(2020,12,16)})
            add(PlanData.create().apply { title = "갓갓"; count = 17; date = LocalDate.of(2020,12,17)})
            add(PlanData.create().apply { title = "갓갓"; count = 18; date = LocalDate.of(2020,12,18)})
            add(PlanData.create().apply { title = "갓갓"; count = 19; date = LocalDate.of(2020,12,19)})
            add(PlanData.create().apply { title = "갓갓"; count = 20; date = LocalDate.of(2020,12,20)})
            add(PlanData.create().apply { title = "갓갓"; count = 21; date = LocalDate.of(2020,12,21)})
        }

        testPlanProject = PlanProject.create(planData)
        testLocalDateRange = LocalDateRange(LocalDate.of(2020,12,1), LocalDate.of(2020,12,21))
        /*trackerVM = TrackerVM(trackerRepository)*/
    }

    @Test
    fun testConverter(){
        val map = TrackerVM.convertTrackerDataMap(testPlanProject, testLocalDateRange)
        println(map)
    }
}