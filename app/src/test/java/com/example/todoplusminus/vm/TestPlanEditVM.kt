package com.example.todoplusminus.vm

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoplusminus.PMCoroutineSpecification
import com.example.todoplusminus.TestCoroutineRule
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.getOrAwaitValue
import com.example.todoplusminus.repository.IPlannerRepository
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.ColorManager
import com.example.todoplusminus.util.TimeHelper
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TestPlanEditVM {

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

        planEditVM = PlanEditVM(repository, PMCoroutineSpecification.MAIN_DISPATCHER)
    }

    @Test
    fun test_setData_notEmptyId() {
        var targetData = PlanData.create().apply {
            title = "갓갓갓"
        }

        testCoroutineRule.runBlockingTest {
            `when`(repository.getPlannerDataById(anyString())).thenReturn(targetData)
            planEditVM.setData(targetData.id)

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
            planEditVM.setData(targetData.id)
            planEditVM.onComplete()

            assertEquals(planEditVM.isEditEnd.getOrAwaitValue().peekContent(), true)
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

            assertEquals(planEditVM.isEditEnd.getOrAwaitValue().peekContent(), true)
        }
    }



}