package com.example.todoplusminus.vm

import android.util.Log
import androidx.lifecycle.*
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.TrackerRepository
import com.example.todoplusminus.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

class TrackerVM(trackerRepo : TrackerRepository) : ViewModel(){

    companion object{

        private val dateHelper = DateHelper()
        private val mJob = Job()

        // planProject를 tracker화면에 보여주기 위한 데이터로 converting한다.
        fun convertTrackerDataMap(_planProject : PlanProject, dateRange : LocalDateRange) : TreeMap<LocalDateRange, List<TrackerData>>{
            var startDate = dateRange.startDate.copy()
            val endDate = dateRange.endDate.copy()

            val planTitleList : List<String> = _planProject.getPlanTitleSet().toList()

            val resultMap : MutableMap<LocalDateRange, List<TrackerData>> = mutableMapOf()

            while(startDate.compareUntilWeek(endDate) <= 0){
                Log.d("godgod", "${startDate.get(WeekFields.ISO.weekOfMonth())}    ${startDate}")
                val weekTrackerList : MutableList<TrackerData> = mutableListOf()
                val weekRange = dateHelper.getWeekDayRangeBy(startDate)

                planTitleList.forEach { title ->
                    val planDataListByTitle = _planProject.getPlanDataListByTitle(title)
                    val trackerData = TrackerData(title, planDataListByTitle?.get(0)?.bgColor ?: ColorManager.getRandomColor())
                    weekTrackerList.add(getTrackerDataByWeekRange(planDataListByTitle ?: mutableListOf() , weekRange, trackerData))
                }

                resultMap[weekRange] = weekTrackerList
                startDate = startDate.customPlusWeeks(1)
            }
            return TreeMap<LocalDateRange, List<TrackerData>>().apply{ putAll(resultMap) }
        }

        private fun getTrackerDataByWeekRange(_sameTitlePlanList : List<PlanData>, weekRange : LocalDateRange, trackerData : TrackerData) : TrackerData{
            val endOfWeekDate = weekRange.endDate  //한주의 마지막 날 ( = 일요일)

            val sortedSameTitlePlanList = _sameTitlePlanList.sortedBy { it.date }

            sortedSameTitlePlanList.forEach { planData ->
                //1주일의 날짜 범위보다 높다면 종료한다.
                if(endOfWeekDate.isBefore(planData.date)) return@forEach

                //전달받은 1주일의 날짜안에 포함되는가?
                if(weekRange.hasContain(planData.date)){
                    trackerData.setTrackerCountByDayOfWeek(planData.count, planData.date.dayOfWeek)
                }
            }
            return trackerData
        }
    }

    private val _allDatePlanProject : LiveData<PlanProject> = trackerRepo.getAllDatePlanProject().asLiveData()

    val trackerDataMap : LiveData<TreeMap<LocalDateRange, List<TrackerData>>> = _allDatePlanProject.switchMap {
        val dateRange = LocalDateRange(it.getOldDate(), LocalDate.now())
        liveData(Dispatchers.Default){
            Log.d("godgod", "${convertTrackerDataMap(it, dateRange)}")
            emit(convertTrackerDataMap(it, dateRange))
        }
    }

}

data class TrackerData(
    val name : String,
    val bgColor : Int = ColorManager.getRandomColor(),
    var mondayCount : Int = 0,
    var tuesdayCount : Int = 0,
    var wednesdayCount : Int = 0,
    var thursdayCount : Int = 0,
    var fridayCount : Int = 0,
    var saturdayCount : Int = 0,
    var sundayCount : Int = 0
){
    fun setTrackerCountByDayOfWeek(count : Int, dayOfWeek : DayOfWeek){
        when(dayOfWeek){
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