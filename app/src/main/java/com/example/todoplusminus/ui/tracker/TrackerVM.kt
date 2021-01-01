package com.example.todoplusminus.ui.tracker

import android.util.Log
import androidx.lifecycle.*
import com.example.todoplusminus.compareUntilWeek
import com.example.todoplusminus.copy
import com.example.todoplusminus.customPlusWeeks
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanProject
import com.example.todoplusminus.data.repository.TrackerRepository
import com.example.todoplusminus.util.*
import com.example.todoplusminus.util.livedata.Event
import kotlinx.coroutines.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class TrackerVM @Inject constructor(trackerRepo: TrackerRepository) : ViewModel() {

    companion object {

        private val dateHelper = DateHelper()
        private val mJob = Job()

        // planProject를 tracker화면에 보여주기 위한 데이터로 converting한다.
        fun convertTrackerDataMap(
            _planProject: PlanProject,
            dateRange: LocalDateRange
        ): TreeMap<LocalDateRange, List<TrackerData>> {

            var startDate = dateRange.startDate.copy()
            val endDate = dateRange.endDate.copy()

            val planTitleList: List<String> = _planProject.getPlanTitleSet().toList()

            //title list가 비어있다면 종료한다... 즉 사용자가 등록한 plan이 없다는 것을 의미함
            if (planTitleList.isEmpty()) return TreeMap()

            val resultMap: MutableMap<LocalDateRange, List<TrackerData>> = mutableMapOf()

            while (startDate.compareUntilWeek(endDate) <= 0) {
                val weekTrackerList: MutableList<TrackerData> = mutableListOf()
                val weekRange = dateHelper.getWeekDayRangeBy(startDate)

                planTitleList.forEach { title ->
                    val planDataListByTitle = _planProject.getPlanDataListByTitle(title)
                    val trackerData =
                        TrackerData(
                            title,
                            planDataListByTitle?.get(0)?.bgColor ?: ColorManager.getRandomColor()
                        )
                    weekTrackerList.add(
                        getTrackerDataByWeekRange(
                            planDataListByTitle ?: mutableListOf(),
                            weekRange,
                            trackerData
                        )
                    )
                }

                resultMap[weekRange] = weekTrackerList
                startDate = startDate.customPlusWeeks(1)
            }
            return TreeMap<LocalDateRange, List<TrackerData>>(Collections.reverseOrder()).apply {
                putAll(
                    resultMap
                )
            }
        }

        private fun getTrackerDataByWeekRange(
            _sameTitlePlanList: List<PlanData>,
            weekRange: LocalDateRange,
            trackerData: TrackerData
        ): TrackerData {
            val endOfWeekDate = weekRange.endDate  //한주의 마지막 날 ( = 일요일)

            val sortedSameTitlePlanList = _sameTitlePlanList.sortedBy { it.date }

            sortedSameTitlePlanList.forEach { planData ->
                //1주일의 날짜 범위보다 높다면 종료한다.
                if (endOfWeekDate.isBefore(planData.date)) return@forEach

                //전달받은 1주일의 날짜안에 포함되는가?
                if (weekRange.hasContain(planData.date)) {
                    trackerData.setTrackerCountByDayOfWeek(planData.count, planData.date.dayOfWeek)
                }
            }
            return trackerData
        }
    }

    private val _allDatePlanProject: LiveData<PlanProject> =
        trackerRepo.getAllDatePlanProject().asLiveData()

    val trackerDataMap: LiveData<TreeMap<LocalDateRange, List<TrackerData>>> =
        _allDatePlanProject.switchMap {
            val dateRange = LocalDateRange(it.getOldDate(), LocalDate.now())
            liveData(Dispatchers.Default) {
                emit(
                    convertTrackerDataMap(
                        it,
                        dateRange
                    )
                )
            }
        }
}

data class TrackerData(
    val name: String,
    val bgColor: Int = ColorManager.getRandomColor(),
    var mondayCount: Int = 0,
    var tuesdayCount: Int = 0,
    var wednesdayCount: Int = 0,
    var thursdayCount: Int = 0,
    var fridayCount: Int = 0,
    var saturdayCount: Int = 0,
    var sundayCount: Int = 0
) {
    fun setTrackerCountByDayOfWeek(count: Int, dayOfWeek: DayOfWeek) {
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> mondayCount = count
            DayOfWeek.TUESDAY -> tuesdayCount = count
            DayOfWeek.WEDNESDAY -> wednesdayCount = count
            DayOfWeek.THURSDAY -> thursdayCount = count
            DayOfWeek.FRIDAY -> fridayCount = count
            DayOfWeek.SATURDAY -> saturdayCount = count
            DayOfWeek.SUNDAY -> sundayCount = count
        }
    }
}