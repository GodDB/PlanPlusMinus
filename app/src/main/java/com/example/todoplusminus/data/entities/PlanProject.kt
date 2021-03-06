package com.example.todoplusminus.data.entities

import com.example.todoplusminus.*
import com.example.todoplusminus.util.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * 모든 플랜을 관리하는 planObject
 *
 * 하나의 planList를 관리하며, 그외 데이터와 밀접한 비즈니스 로직을 처리한다.
 * */
class PlanProject private constructor() {

    companion object {
        fun create(plandataList: List<PlanData>?): PlanProject =
            PlanProject().apply {
                setPlanDatas(plandataList ?: mutableListOf())
            }
    }

    private var mPlanDataList: MutableList<PlanData> = mutableListOf()

    //이름순으로 planDataList 담겨있는 map
    private var mPlanDataMapByTitle: MutableMap<String, MutableList<PlanData>> = mutableMapOf()
    private val dateHelper = DateHelper()

    fun setPlanDatas(planList: List<PlanData>) {
        mPlanDataList.clear()
        mPlanDataList.addAll(planList)

        initializePlanDataMap()
    }

    fun addPlanData(planData: PlanData) {
        mPlanDataList.add(planData)

        mPlanDataMapByTitle[planData.title]?.add(planData)
    }

    fun getPlanDataByIndex(index: Int) : PlanData {
        if (!checkWhetherOutOfBound(index)) return mPlanDataList[index]
        return PlanData.create()
    }

    fun getPlanDataList() = mPlanDataList.toList()

    fun getPlanDataListByTitle(title : String) = mPlanDataMapByTitle[title]

    fun getPlanTitleSet() : Set<String>{
        val planTitleSet : MutableSet<String> = mutableSetOf()

        mPlanDataList.forEach {
            planTitleSet.add(it.title)
        }

        return planTitleSet
    }

    fun getPlanDataById(id: BaseID): PlanData {
        mPlanDataList.forEach { planData ->
            if (planData.id == id) return planData
        }
        return PlanData.create()
    }

    fun getPlanDataListByDate(date: LocalDate): List<PlanData> {
        val result: MutableList<PlanData> = mutableListOf()

        mPlanDataList.forEach { planData ->
            if (planData.date == date) result.add(planData)
        }

        return result
    }

    fun increasePlanDataCountByIndex(count: Int, index: Int) {
        if (!checkWhetherOutOfBound(index)) mPlanDataList[index].increaseCount(count)
    }

    fun getPlanDataBgColorByIndex(index: Int): Int {
        if (!checkWhetherOutOfBound(index)) return mPlanDataList[index].bgColor
        return PlanData.DEFAULT_BG_COLOR
    }

    fun getPlanDataIdByIndex(index: Int): BaseID {
        if (!checkWhetherOutOfBound(index)) return mPlanDataList[index].id
        return BaseID.createEmpty()
    }

    fun getPlanDataTitleByIndex(index: Int): String {
        if (!checkWhetherOutOfBound(index)) return mPlanDataList[index].title
        return ""
    }

    fun getPlanDataCountByIndex(index: Int): Int {
        if (!checkWhetherOutOfBound(index)) return mPlanDataList[index].count
        return 0
    }

    private fun checkWhetherOutOfBound(value: Int): Boolean {
        return value >= mPlanDataList.size
    }

    private fun initializePlanDataMap(){
        mPlanDataMapByTitle.clear()

        val titleList = getPlanTitleSet().toList()

        titleList.forEach { title ->
            val planDataListBySameTitle : MutableList<PlanData> = mutableListOf()

            mPlanDataList.forEach {
                if(title == it.title) planDataListBySameTitle.add(it)
            }
            mPlanDataMapByTitle[title] = planDataListBySameTitle
        }
    }
    //todo calculate logic

    fun getTotalCount(): Int = sum(mPlanDataList)

    private fun sum(list: List<PlanData>): Int {
        var result = 0
        list.forEach {
            result += it.count
        }
        return result
    }

    /**
     * 전달받은 date범위 안에서 주간 Count list를 전달한다.
     * */
    fun getWeekCountListBetween(_startDate: LocalDate, endDate: LocalDate): List<List<Int>> {
        // 가장 옛날 date보다 낮다면 실행하지 않는다.
        if (getOldDate().isAfter(_startDate)) return emptyList()

        val weekDataList: MutableList<List<Int>> = mutableListOf()
        var startDate = _startDate.copy()

        // startDate가 endDate와 같은 주 일때까지 반복한다.
        while (startDate.compareUntilWeek(endDate) <= 0) {
            val list: List<Int> = getWeekDataInclude(startDate).map { it.count }
            weekDataList.add(list)
            startDate = startDate.customPlusWeeks(1)
        }

        return weekDataList
    }

    /**
     * 전달받은 date범위 안에서 월간 Count list를 전달한다.
     * */
    fun getMonthCountListBetween(_startDate: LocalDate, endDate: LocalDate): List<List<Int>> {
        // 가장 옛날 date보다 낮다면 실행하지 않는다.
        if (getOldDate().isAfter(_startDate)) return emptyList()

        val monthDataList: MutableList<List<Int>> = mutableListOf()
        var startDate = _startDate.copy()

        // startDate가 endDate와 같은 달 일때까지 반복한다.
        while (startDate.compareUntilMonth(endDate) <= 0) {
            val list: List<Int> = getMonthDataInclude(startDate).map { it.count }
            monthDataList.add(list)
            startDate = startDate.plusMonths(1)
        }

        return monthDataList
    }

    /**
     * 전달받은 date범위 안에서 연간 Count list를 전달한다
     * */
    fun getYearCountListBetween(_startDate: LocalDate, endDate: LocalDate): List<List<Int>> {
        //가장 옛날 date보다 낮다면 실행하지 않는다
        if (getOldDate().isAfter(_startDate)) return emptyList()

        val yearDataList: MutableList<List<Int>> = mutableListOf()
        var startDate = _startDate

        // starDate가 endDate와 같은 년도 일때까지 반복한다.
        while (startDate.compareUntilYear(endDate) <= 0) {
            val list = getYearDataInclude(startDate)
            var oneYearCountList: MutableList<Int> = mutableListOf()

            //1년치 데이터
            list.forEachIndexed { month, planList ->
                var monthCount = 0

                //1달치 데이터
                planList.forEach { planData ->
                    //하루치 데이터
                    monthCount += planData.count
                }
                oneYearCountList.add(monthCount)
            }

            yearDataList.add(oneYearCountList)
            startDate = startDate.plusYears(1)
        }

        return yearDataList
    }


    /**
     * 전달받은 date를 포함하는 1주일치 데이터를 전달한다.
     *
     * 단위는 1일
     * */

    //todo - plandata.create()는 현재 요일을 default값으로 생성하기 때문에 추후 문제 발생 가능성이 있다..

    private fun getWeekDataInclude(date: LocalDate): List<PlanData> {
        val range = dateHelper.getWeekDayRangeBy(date)

        val list = MutableList<PlanData>(7) { PlanData.create() }

        mPlanDataList.forEach { planData ->
            //dateRange에 포함이 된다면
            if (range.hasContain(planData.date)) {
                list[planData.date.dayOfWeek.value - 1] = planData
            }
        }

        return list
    }

    /**
     * 전달받은 date를 포함하는 1달치 데이터를 전달한다.
     *
     * 단위는 1일
     * */
    private fun getMonthDataInclude(date: LocalDate): List<PlanData> {
        val monthRange = dateHelper.getMonthRangeBy(date)

        //배열의 마지막 index(= 마지막 날)
        val endIndex = monthRange.endDate.dayOfMonth
        val list = MutableList(endIndex) {
            PlanData.create()
        }

        mPlanDataList.forEach { planData ->
            if (monthRange.hasContain(planData.date)) {
                list[planData.date.dayOfMonth - 1] = planData
            }
        }

        return list
    }

    /**
     * 전달받은 date를 포함하는 1년치 데이터를 전달한다.
     *
     * 단위는 1달 - 1일
     * */
    fun getYearDataInclude(date: LocalDate): List<List<PlanData>> {
        val yearRange = dateHelper.getYearRangeBy(date)

        //배열의 마지막 index (= 마지막 달)
        val yearList: MutableList<List<PlanData>> = mutableListOf()

        for (i in yearRange.startDate.monthValue..yearRange.endDate.monthValue) {
            val list = getMonthDataInclude(LocalDate.of(yearRange.startDate.year, i, 1))
            yearList.add(list)
        }

        return yearList
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
    fun getLatestDate(): LocalDate {
        var diffDate = LocalDate.MIN
        var diffDay = Long.MAX_VALUE
        var result: LocalDate = LocalDate.MIN

        mPlanDataList.forEach {
            if (diffDay > ChronoUnit.DAYS.between(it.date, diffDate)) {
                diffDay = ChronoUnit.DAYS.between(it.date, diffDate)
                result = it.date
            }
        }

        return result
    }

}