package com.example.todoplusminus

import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.util.TimeHelper
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.Instant
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField

@RunWith(MockitoJUnitRunner::class)
class PlanProjectTest {

    lateinit var project : PlanProject

    @Before
    fun setUp(){
        val planData1 = PlanData.create().apply {
            date = LocalDate.of(2020,11,2)
            count = 3
        }
        val planData2 = PlanData.create().apply {
            date = LocalDate.of(2020,11,9)
            count = 5
        }
        val planData3 = PlanData.create().apply {
            date = LocalDate.of(2020,11,16)
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

        assertEquals(oldDate, LocalDate.of(2020, 11, 2))
    }

    @Test
    fun test_getNewDate(){
        val newDate = project.getLatestDate()

        assertEquals(newDate, LocalDate.of(2020,11, 16))
    }

    @Test
    fun test_getWeekCountList(){
        val result = project.getWeekCountListBetween(LocalDate.of(2020,11,2), LocalDate.of(2020,11,7))

        assertEquals(result.size, 1)

    }

    @Test
    fun aaa(){
        val range = TimeHelper.getWeekDayRangeBy(LocalDate.now())

        println(range.startDate.dayOfWeek.value)
        println(range.startDate.dayOfWeek.name)
        println(range.endDate.dayOfWeek.value)
        println(range.endDate.dayOfWeek.name)
    }

}