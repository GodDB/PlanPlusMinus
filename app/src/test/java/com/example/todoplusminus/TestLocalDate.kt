package com.example.todoplusminus

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class TestLocalDate {

    @Before
    fun setup(){

    }

    @Test
    fun testWeekValue(){
        print(LocalDate.of(2021,1,1).getWeekValue())

    }

    private fun LocalDate.getWeekValue()= this.get(WeekFields.ISO.weekOfMonth())

}