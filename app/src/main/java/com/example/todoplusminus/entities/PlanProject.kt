package com.example.todoplusminus.entities

import android.util.Log
import com.example.todoplusminus.util.LocalDateRange
import com.example.todoplusminus.util.copy
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.math.min

/**
 * 모든 플랜을 관리하는 planObject
 *
 * 하나의 planList를 관리하며, 그외 데이터와 밀접한 비즈니스 로직을 처리한다.
 * */
class PlanProject private constructor() {

    companion object {
        fun create(plandataList: List<PlanData>?): PlanProject =
            PlanProject().apply {
                setPlanDatas(plandataList ?: mutableListOf(PlanData.create()))
            }
    }

    private var mPlanDataList: MutableList<PlanData> = mutableListOf(PlanData.create())

    fun setPlanDatas(planList: List<PlanData>) {
        mPlanDataList.clear()
        mPlanDataList.addAll(planList)
    }

    fun addPlanData(planData: PlanData) {
        mPlanDataList.add(planData)
    }

    fun getPlanDataByIndex(index: Int) = mPlanDataList[index]

    fun getPlanDataList() = mPlanDataList.toList()

    fun getPlanDataById(id: String): PlanData {
        mPlanDataList.forEach { planData ->
            if (planData.id == id) return planData
        }
        return PlanData.create()
    }

    fun increasePlanDataCountByIndex(count: Int, index: Int) {
        mPlanDataList[index].increaseCount(count)
    }

    fun getPlanDataBgColorByIndex(index: Int) = mPlanDataList[index].bgColor
    fun getPlanDataIdByIndex(index: Int) = mPlanDataList[index].id
    fun getPlanDataTitleByIndex(index: Int) = mPlanDataList[index].title
    fun getPlanDataCountByIndex(index: Int) = mPlanDataList[index].count

    //todo calculate logic

    fun getTotalCount(): Int {
        var result = 0
        mPlanDataList.forEach {
            result += it.count
        }
        return result
    }

    /**
     * 전달받은 date범위 안에서 데이터를 1주일 단위로 전달받는다.
     * */
/*    fun getWeekDataListBetween(_startDate : LocalDate, endDate : LocalDate) : List<List<PlanData>>{
        // 가장 옛날 date보다 낮다면 실행하지 않는다.
        if(getOldDate().isAfter(_startDate)) return emptyList()
        val weekDataList : MutableList<List<PlanData>> = mutableListOf()

        var startDate = _startDate
        // startDate가 endDate보다 작을때까지 반복한다.
        while(!startDate.isAfter(endDate)){
            val list = getWeekDataInclude(startDate)

            weekDataList.add(list)
            startDate = startDate.plusWeeks(1)
        }
        return weekDataList
    }*/

    fun getWeekCountListBetween(_startDate : LocalDate, endDate : LocalDate) : List<List<Int>> {
        // 가장 옛날 date보다 낮다면 실행하지 않는다.
        if (getOldDate().isAfter(_startDate)) return emptyList()

        val weekDataList: MutableList<List<Int>> = mutableListOf()

        var startDate = _startDate.copy()
        // startDate가 endDate와 같은 주 일때까지 반복한다.
        while (ChronoUnit.WEEKS.between(startDate, endDate) > -1) {
            val list : List<Int> = getWeekDataInclude(startDate).map { it.count }
            weekDataList.add(list)
            startDate = startDate.plusWeeks(1)
        }

        return weekDataList
    }
    /**
     * 전달받은 date를 포함하는 1주일치 데이터를 전달한다.
     *
     * 단위는 1일
     * */
    private fun getWeekDataInclude(date: LocalDate) : List<PlanData> {
        val range = getWeekDayRangeBy(date)

        val list = MutableList<PlanData>(7) { PlanData.create() }

        mPlanDataList.forEach { planData ->
            //dateRange에 포함이 된다면
            if (range.hasContain(planData.date)) {
                list[planData.date.dayOfWeek.value -1] = planData
            }
        }

        return list
    }

    /**
     * 전달받은 date를 포함하는 1달치 데이터를 전달한다.
     *
     * 단위는 1일
     * */
    fun getMonthDataInclude(date: LocalDate) : List<PlanData>{
        val monthRange = getMonthRangeBy(date)

        //배열의 마지막 index(= 마지막 날)
        val endDay = monthRange.endDate.dayOfMonth
        val list = MutableList(endDay){
            PlanData.create()
        }

        mPlanDataList.forEach {planData ->
            if(monthRange.hasContain(planData.date)){
                list[planData.date.dayOfMonth-1] = planData
            }
        }

        return list
    }

    /**
     * 전달받은 date를 포함하는 1년치 데이터를 전달한다.
     *
     * 단위는 1달
     * */
    fun getYearDataInclude(date: LocalDate) {

    }

    /**
     * PlanData들 중 가장 옛날 date를 구한다.
     * */
    fun getOldDate(): LocalDate {

        var diffDate = LocalDate.now()
        var diffDay = Long.MIN_VALUE
        var result: LocalDate = LocalDate.now()

        mPlanDataList.forEach {
            if (diffDay < ChronoUnit.DAYS.between(it.date, diffDate)) {
                diffDay = ChronoUnit.DAYS.between(it.date, diffDate)
                result = it.date
            }
        }

        return result
    }

    /**
     * PlanData들 중 가장 최신 date를 구한다.
     * */
    fun getNewDate(): LocalDate {
        var diffDate = LocalDate.now()
        var diffDay = Long.MAX_VALUE
        var result: LocalDate = LocalDate.now()

        mPlanDataList.forEach {
            if (diffDay > ChronoUnit.DAYS.between(it.date, diffDate)) {
                diffDay = ChronoUnit.DAYS.between(it.date, diffDate)
                result = it.date
            }
        }

        return result
    }

    /**
     * 전달받은 date를 바탕으로 1주일의 범위를 전달해준다.
     * */
    private fun getWeekDayRangeBy(date: LocalDate): LocalDateRange {
        val weekNum = date.dayOfWeek.value

        val startDate = date.minusDays(abs(1L - weekNum))
        val endDate = date.plusDays(abs(weekNum - 7L))

        return LocalDateRange(startDate, endDate)
    }

    /**
     * 전달받은 date를 바탕으로 1달의 범위를 전달해준다.
     * */
    private fun getMonthRangeBy(date : LocalDate) : LocalDateRange{
        val startDate = LocalDate.of(date.year, date.monthValue, 1) //해당 달의 첫날
        val endDate = LocalDate.of(date.year, date.monthValue, date.lengthOfMonth()) //해당 달의 마지막 날

        return LocalDateRange(startDate, endDate)
    }
}