package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.LocalDateRange
import com.example.todoplusminus.util.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PlanHistoryContentVM(
    _mode: String,
    private val targetId: String,
    private val repository: PlannerRepository
) {

    companion object {
        const val MODE_WEEK = "week_mode"
        const val MODE_MONTH = "month_mode"
        const val MODE_YEAR = "year_mode"
    }

    private val _mPlanProject: PlanProject =
        PlanProject.create(runBlocking(Dispatchers.IO) { repository.getAllPlanDataById(targetId) })

    val mode: LiveData<Event<String>> = MutableLiveData(Event(_mode))

    //chart data --------------------
    val mGraphBarColor: LiveData<Int>
        get() = MutableLiveData(_mPlanProject.getPlanDataBgColorByIndex(0))

    val yData: List<List<Int>>?
        get() {
            val dateHelper = DateHelper()

            return when (mode.value?.peekContent()) {
                MODE_WEEK ->
                    _mPlanProject.getWeekCountListBetween(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    ).reversed()
                MODE_MONTH ->
                    _mPlanProject.getMonthCountListBetween(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    ).reversed()
                MODE_YEAR ->
                    _mPlanProject.getYearCountListBetween(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    ).reversed()
                else -> emptyList()
            }
        }

    val xData: List<List<String>>
        get() {
            val dateHelper = DateHelper()

            return when (mode.value?.peekContent()) {
                MODE_WEEK -> dateHelper.getDayOfWeekList(
                    LocalDateRange(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
                MODE_MONTH -> dateHelper.getDayOfMonthList(
                    LocalDateRange(
                        _mPlanProject.getOldDate(),
                       DateHelper.getCurDate()
                    )
                ).reversed()
                MODE_YEAR -> dateHelper.getYearList(
                    LocalDateRange(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
                else -> emptyList()
            }
        }

    val graphTitle: List<LocalDateRange>
        get() {
            val dateHelper = DateHelper()

            return when (mode.value?.peekContent()) {
                MODE_WEEK -> dateHelper.getWeekRangeList(
                    LocalDateRange(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
                MODE_MONTH -> dateHelper.getMonthRangeList(
                    LocalDateRange(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
                MODE_YEAR -> dateHelper.getYearRangeList(
                    LocalDateRange(
                        _mPlanProject.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
                else -> emptyList()
            }
        }

    //summary data --------------------------

    val summaryTargetIndex: MutableLiveData<Int> = MutableLiveData(0)


    val summaryAverage: LiveData<Int> = Transformations.switchMap(summaryTargetIndex) { index ->
        val value = yData?.get(index)?.sum()
        val count = xData[index].size

        val average = value?.div(count)

        MutableLiveData(average ?: 0)
    }

    val summaryAccumulation: LiveData<Int> =
        Transformations.switchMap(summaryTargetIndex) { index ->
            val value = yData?.get(index)?.sum()

            MutableLiveData(value ?: 0)
        }

    val totalAccumulation: LiveData<Int>
        get() = MutableLiveData(_mPlanProject.getTotalCount())


}