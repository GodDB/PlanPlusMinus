package com.example.todoplusminus

import com.example.todoplusminus.util.DateHelper
import com.example.todoplusminus.util.LocalDateRange
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class TestDateHelper {

    private val dateHelper = DateHelper()
    /*@Test
    fun makeMonthDate(){
        print(DateHelper.makeMonthDate(LocalDate.of(2020, 12, 13)))
    }*/

    @Test
    fun getCalendarBy(){
        val dateRange = LocalDateRange(LocalDate.of(2020, 11, 1), LocalDate.of(2020, 12, 15))

        val expected = dateHelper.getCalendarBy(dateRange)

        val actual = listOf<List<Int>>(
            listOf(0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30),
            listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun test_getWeekDayRangeBy(){
        val dateHelper = DateHelper()
        val test1 = LocalDate.of(2020, 11, 1)
        val test2 = LocalDate.of(2020, 11, 21)
        val test3 = LocalDate.of(2020, 11, 30)
        val test4 = LocalDate.of(2020, 12, 15)

        val expected1 = dateHelper.getWeekDayRangeBy(test1)
        val expected2 = dateHelper.getWeekDayRangeBy(test2)
        val expected3 = dateHelper.getWeekDayRangeBy(test3)
        val expected4 = dateHelper.getWeekDayRangeBy(test4)

        val actual1 = LocalDateRange(LocalDate.of(2020, 10, 26), LocalDate.of(2020,11,1))
        val actual2 = LocalDateRange(LocalDate.of(2020,11, 16), LocalDate.of(2020, 11, 22))
        val actual3 = LocalDateRange(LocalDate.of(2020,11, 30), LocalDate.of(2020,12,6))
        val actual4 = LocalDateRange(LocalDate.of(2020, 12, 14), LocalDate.of(2020,12, 20))

        assertEquals(expected1, actual1)
        assertEquals(expected2, actual2)
        assertEquals(expected3, actual3)
        assertEquals(expected4, actual4)
    }

    @Test
    fun getCalendarBy2(){
        val dateHelper = DateHelper()

        val localRange = LocalDateRange(LocalDate.of(2020,10, 1), LocalDate.of(2020, 12, 11))

        print(dateHelper.getCalendarBy2(localRange))

    }
}