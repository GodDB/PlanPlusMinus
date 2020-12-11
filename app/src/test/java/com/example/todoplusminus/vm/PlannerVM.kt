package com.example.todoplusminus.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.PMCoroutineSpecification
import com.example.todoplusminus.TestCoroutineRule
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import com.example.todoplusminus.getOrAwaitValue
import com.example.todoplusminus.repository.IPlannerRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlannerVM {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var planVM: PlannerViewModel

    @Mock
    lateinit var repository: IPlannerRepository

    private val targetPlanData1 = PlanData.create().apply {
        title = "target1"
    }
    private val targetPlanData2 = PlanData.create().apply {
        title = "target2"
    }
    private val targetPlanData3 = PlanData.create().apply {
        title = "target3"
    }


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        runBlocking {
            Mockito.`when`(repository.getAllPlanDataByDate(LocalDate.now())).thenReturn(
                MutableLiveData(
                    mutableListOf(
                        targetPlanData1,
                        targetPlanData2,
                        targetPlanData3
                    )
                )
            )

            Mockito.`when`(repository.refreshPlannerData(LocalDate.now())).thenReturn(Unit)
            Mockito.`when`(repository.getMemoByDate(LocalDate.now())).thenReturn(MutableLiveData(
                PlanMemo.create()
            ))

            planVM = PlannerViewModel(repository, PMCoroutineSpecification.MAIN_DISPATCHER)

            //plan project에 대해서 옵저빙을 실시한다.
            planVM.planProject.getOrAwaitValue()
        }
    }

    @Test
    fun test_onItemDelete(){
        runBlocking {
            planVM.onItemDelete(0)

            assertEquals(planVM.planProject.value?.getPlanDataByIndex(0)?.id, targetPlanData1.id)
            assertEquals(planVM.planProject.value?.getPlanDataByIndex(1)?.id, targetPlanData2.id)
            assertEquals(planVM.planProject.value?.getPlanDataByIndex(2)?.id, targetPlanData3.id)
            Mockito.verify(repository).deletePlannerDataById(targetPlanData1.id)
        }
    }

    @Test
    fun test_switchEditMode_when_editmode(){
        testCoroutineRule.runBlockingTest {
            planVM.isEditMode.value = true

            planVM.switchEditMode()

            assertEquals(planVM.isEditMode.getOrAwaitValue(), false)
            Mockito.verify(repository).updatePlannerDataList(planVM.planProject.getOrAwaitValue().getPlanDataList())
        }
    }

    @Test
    fun test_switchEditMode_when_basicMode(){
        testCoroutineRule.runBlockingTest {
            planVM.isEditMode.value = false

            planVM.switchEditMode()

            assertEquals(planVM.isEditMode.getOrAwaitValue(), true)/*
            Mockito.verify(repository).updatePlannerDataList(planVM.planProject.getOrAwaitValue().getPlanDataList())*/
        }
    }

    @Test
    fun test_onItemClick_when_editMode(){
        testCoroutineRule.runBlockingTest {
            planVM.isEditMode.value = true

            //not empty id
            planVM.onItemClick(targetPlanData2.id)
            assertEquals(planVM.showEditPlanDataID.getOrAwaitValue()?.peekContent(), targetPlanData2.id)

            //empty id
            planVM.onItemClick(null)
            assertEquals(planVM.showEditPlanDataID.getOrAwaitValue()?.peekContent(), PlanData.EMPTY_ID)
        }
    }

    @Test
    fun test_onItemClick_when_basicMode(){
        testCoroutineRule.runBlockingTest {
            planVM.isEditMode.value = false

            //not empty id
            planVM.onItemClick(targetPlanData3.id)
            assertEquals(planVM.showHistoryId.getOrAwaitValue().getContentIfNotHandled(), targetPlanData3.id)

            //empty id
            /*planVM.onItemClick("")
            assertEquals(planVM.showHistoryId.getOrAwaitValue().getContentIfNotHandled(), "")*/



        }
    }
}