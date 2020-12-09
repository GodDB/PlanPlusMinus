package com.example.todoplusminus

import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class PlanProjectTest {

    lateinit var project : PlanProject

    @Before
    fun setUp(){
        val planData1 = PlanData.create().apply {
            date = LocalDate.of(2020,11,1)
            count = 3
        }
        val planData2 = PlanData.create().apply {
            date = LocalDate.of(2020,11,8)
            count = 5
        }
        val planData3 = PlanData.create().apply {
            date = LocalDate.of(2020,11,15)
            count = 3
        }

        project = PlanProject.create(listOf(planData1, planData2, planData3))
    }

    @Test
    fun test_getTotalCount(){
        val totalCount = project.getTotalCount()

        assertEquals(totalCount, 11)
    }

    @Test
    fun test_getOldDate(){
        val oldDate = project.getOldDate()

        assertEquals(oldDate, LocalDate.of(2020, 11, 1))
    }

    @Test
    fun test_getNewDate(){
        val newDate = project.getLatestDate()

        assertEquals(newDate, LocalDate.of(2020,11, 15))
    }

}