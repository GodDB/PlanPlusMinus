package com.example.todoplusminus

import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.util.ColorManager
import com.example.todoplusminus.util.compareUntilWeek
import net.bytebuddy.asm.Advice
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Test
    fun test_planProject_getOldDate() {
        val planProject = mock(PlanProject::class.java)

        val planData = mock(PlanData::class.java)

        assertEquals(planData.date, LocalDate.now())

    }

    @Test
    fun getColor(){
        print(ColorManager.getRandomColor())
    }

}