package com.example.todoplusminus.ui.main.history.chart

import android.graphics.Typeface
import androidx.lifecycle.*
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.util.livedata.ThreeCombinedLiveData
import com.example.todoplusminus.util.livedata.TwoCombinedLiveData
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.entities.PlanProject
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.data.repository.PlannerRepository
import com.example.todoplusminus.util.LocalDateRange
import com.example.todoplusminus.util.DateHelper
import javax.inject.Inject

class PlanHistoryContentVM @Inject constructor(
    private val _mode: Mode,
    private val targetId: BaseID,
    private val repository: IPlannerRepository
) {

    class Mode private constructor(
        private val modeName : String
    ){
        companion object {
            const val MODE_WEEK = "week_mode"
            const val MODE_MONTH = "month_mode"
            const val MODE_YEAR = "year_mode"

            fun createWeekMode() : Mode = Mode(MODE_WEEK)
            fun createMonthMode() : Mode = Mode(MODE_MONTH)
            fun createYearMode() : Mode = Mode(MODE_YEAR)
        }

        override fun toString(): String {
            return modeName
        }

        override fun equals(other: Any?): Boolean {
            val otherMode = (other as? Mode)?: return false
            return this.modeName == otherMode.modeName
        }

        override fun hashCode(): Int {
            return modeName.hashCode()
        }
    }


    val font: Typeface?
        get() = AppConfig.font

    private val _mPlanProject: LiveData<PlanProject> =
        repository.getPlanProjectById(targetId).asLiveData()

    val mode: LiveData<Event<Mode>> = MutableLiveData(
        Event(_mode)
    )

    //chart data --------------------
    val mGraphBarColor: LiveData<Int> = _mPlanProject.switchMap {
        MutableLiveData(it.getPlanDataBgColorByIndex(0))
    }

    val yData: LiveData<List<List<Int>>> = _mPlanProject.switchMap {
        val data = when (mode.value?.peekContent().toString()) {
            Mode.MODE_WEEK -> {
                it.getWeekCountListBetween(
                    it.getOldDate(),
                    DateHelper.getCurDate()
                ).reversed()
            }

            Mode.MODE_MONTH -> {
                it.getMonthCountListBetween(
                    it.getOldDate(),
                    DateHelper.getCurDate()
                ).reversed()
            }

            Mode.MODE_YEAR -> {
                it.getYearCountListBetween(
                    it.getOldDate(),
                    DateHelper.getCurDate()
                ).reversed()
            }
            else -> emptyList()
        }

        MutableLiveData(data)
    }

    private val dateHelper = DateHelper()

    val xData: LiveData<List<List<String>>> = _mPlanProject.switchMap {
        val data = when (mode.value?.peekContent().toString()) {
            Mode.MODE_WEEK -> {
                dateHelper.getDayOfWeekList(
                    LocalDateRange(
                        it.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
            }

            Mode.MODE_MONTH -> {
                dateHelper.getDayOfMonthList(
                    LocalDateRange(
                        it.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
            }

            Mode.MODE_YEAR -> {
                dateHelper.getYearList(
                    LocalDateRange(
                        it.getOldDate(),
                        DateHelper.getCurDate()
                    )
                ).reversed()
            }
            else -> emptyList()
        }

        MutableLiveData(data)
    }


    val graphTitle: LiveData<List<LocalDateRange>> = _mPlanProject.switchMap {
        val data = when (mode.value?.peekContent().toString()) {
            Mode.MODE_WEEK -> dateHelper.getWeekRangeList(
                LocalDateRange(
                    it.getOldDate(),
                    DateHelper.getCurDate()
                )
            ).reversed()
            Mode.MODE_MONTH -> dateHelper.getMonthRangeList(
                LocalDateRange(
                    it.getOldDate(),
                    DateHelper.getCurDate()
                )
            ).reversed()
            Mode.MODE_YEAR -> dateHelper.getYearRangeList(
                LocalDateRange(
                    it.getOldDate(),
                    DateHelper.getCurDate()
                )
            ).reversed()
            else -> emptyList()
        }

        MutableLiveData(data)
    }


    //summary data --------------------------

    val summaryTargetIndex: MutableLiveData<Int> = MutableLiveData(0)

    val summaryAverage: ThreeCombinedLiveData<Int, List<List<String>>, List<List<Int>>, Int> =
        ThreeCombinedLiveData(
            summaryTargetIndex,
            xData,
            yData
        ) { index, xData, yData ->
            val value = yData[index].sum()
            val count = xData[index].size

            value.div(count)
        }

    val summaryAccumulation: TwoCombinedLiveData<Int, List<List<Int>>, Int> =
        TwoCombinedLiveData(
            summaryTargetIndex,
            yData
        ) { index, yData ->
            yData[index].sum()
        }

    val totalAccumulation: LiveData<Int> = _mPlanProject.switchMap {
        MutableLiveData(it.getTotalCount())
    }


}