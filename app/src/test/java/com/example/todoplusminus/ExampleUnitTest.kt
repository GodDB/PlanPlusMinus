package com.example.todoplusminus

import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.util.ColorManager
import com.example.todoplusminus.util.compareUntilWeek
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import net.bytebuddy.asm.Advice
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.Flow

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

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

    @Test
    fun flowTest(){
        testCoroutineRule.runBlockingTest {
            println("calling foo..")
            val flow = foo()
            println("calling collect...")
            flow.collect { value -> println(value) }
            println("calling collect again...")
            flow.collect { value -> println(value) }
        }
    }

    private fun foo() = flow{
        println("Flow started")
        for ( i in 1..3){
            delay(100)
            emit(i)
        }
    }


    @Test
    fun flowTest2(){
        testCoroutineRule.runBlockingTest {

            (1..5).asFlow()
                .buffer()
                /*.filter {
                    println("Filter $it")
                    it % 2 == 0
                }
                .map {
                    println("Map $it")
                    "string $it"
                }*/
                .collect{
                    println("collect $it")
                }

        }
    }
}