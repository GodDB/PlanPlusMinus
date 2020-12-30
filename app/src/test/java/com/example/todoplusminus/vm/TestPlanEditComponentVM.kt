package com.example.todoplusminus.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoplusminus.TestCoroutineRule
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.getOrAwaitValue
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.ui.main.edit.PlanEditVM
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TestPlanEditComponentVM {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var planEditVM: PlanEditVM

    @Mock
    lateinit var repository: IPlannerRepository



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        planEditVM =
            PlanEditVM(repository)
    }
    @Test
    fun test_setData_notEmptyId() {
        var targetData = PlanData.create().apply {
            title = "갓갓갓"
        }

        testCoroutineRule.runBlockingTest {
            `when`(repository.getPlannerDataById(anyString())).thenReturn(targetData)
            planEditVM.setId(targetData.id)

            assertEquals(planEditVM.mId, targetData.id)
            assertEquals(planEditVM.mTitle.getOrAwaitValue(), targetData.title)
            assertEquals(planEditVM.mBgColor.getOrAwaitValue(), targetData.bgColor)
        }
    }

    @Test
    fun test_onComplete_WithoutId(){
        var targetData = PlanData.create().apply {
            id = PlanData.EMPTY_ID
        }

        testCoroutineRule.runBlockingTest {
            planEditVM.setId(targetData.id)
            planEditVM.onComplete()

            assertEquals(planEditVM.isEditClose.getOrAwaitValue().peekContent(), true)
        }
    }
    @Test
    fun test_onComplete_Id(){
        var id = UUID.randomUUID().toString()
        var title = "Dddwdaf"
        var bgcolor = 248905

        testCoroutineRule.runBlockingTest {
            planEditVM.mBgColor.value = bgcolor
            planEditVM.mTitle.value = title
            planEditVM.mId = id

            planEditVM.onComplete()

            assertEquals(planEditVM.isEditClose.getOrAwaitValue().peekContent(), true)
        }
    }

    @Test
    fun test(){

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DATE, 1)

        val currentMonthMaxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        println(currentMonthMaxDate)


        val prevMonthTail = calendar.get(Calendar.DAY_OF_WEEK) -1
        println(prevMonthTail)

        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        val maxOffsetDate = maxDate - prevMonthTail

        println(maxOffsetDate)
    }



}