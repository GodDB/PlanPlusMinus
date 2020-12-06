package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.TimeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PlanHistoryContentVM(
    _mode: String,
    private val targetId: String,
    private val repository: PlannerRepository
) {

    private val _mPlanProject: PlanProject = PlanProject.create(runBlocking(Dispatchers.IO){ repository.getAllPlanDataById(targetId) })

    val mode: LiveData<Event<String>> = MutableLiveData(Event(_mode))

    val yData: List<List<Int>>?
        get() {
            //todo 완성하
            return when (mode.value?.peekContent()) {
                MODE_WEEK ->
                    _mPlanProject.getWeekCountListBetween(
                        _mPlanProject.getOldDate(),
                        TimeHelper.getCurDate()
                    )
                MODE_MONTH -> emptyList()
                MODE_YEAR -> emptyList()
                else -> emptyList()
            }
        }

    val xData: List<String>
        get() {
            //todo 임시로 처리함... 이 배열들을 누가 관리하는게 좋을까
            return when (mode.value?.peekContent()) {
                MODE_WEEK -> listOf("월", "화", "수", "목", "금", "토", "일")
                MODE_MONTH -> listOf(
                    "1", "3", "5",
                    "7", "9", "11",
                    "13", "15", "17",
                    "19", "21", "23",
                    "25", "27", "29"
                )
                MODE_YEAR -> listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
                else -> emptyList<String>()
            }
        }

    companion object {
        const val MODE_WEEK = "week_mode"
        const val MODE_MONTH = "month_mode"
        const val MODE_YEAR = "year_mode"
    }
}