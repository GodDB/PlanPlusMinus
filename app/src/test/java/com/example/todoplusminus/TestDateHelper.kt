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
}