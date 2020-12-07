package com.example.todoplusminus.util

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.abs

object TimeHelper {

    fun getCurAllDate(): String {
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return result.format(date)
    }


    fun getCurDate(): LocalDate = LocalDate.now()

    fun getCurDateTime(): String {
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("MM/dd\n a HH:mm")
        return result.format(date)
    }

    fun getCurTime(): String {
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("HH:mm:ss")
        return result.format(date)
    }

    //todo 다국어 처리
    //전달받은 dateRange의 주별 날짜리스트를 전달한다.
    fun getDayOfWeekList(dateRange: LocalDateRange): List<List<String>> {

        var startDate = dateRange.startDate.copy()
        var endDate = dateRange.endDate.copy()

        val dayOfWeekList: MutableList<List<String>> = mutableListOf()

        while (ChronoUnit.WEEKS.between(startDate, endDate) > -1) {
            val dayList = listOf("월", "화", "수", "목", "금", "토", "일")

            dayOfWeekList.add(dayList)
            startDate = startDate.plusWeeks(1)
        }

        return dayOfWeekList
    }


    //전달받은 dateRange의 월별 날짜리스트를 전달한다.
    fun getDayOfMonthList(dateRange: LocalDateRange): List<List<String>> {

        var startDate = dateRange.startDate.copy()
        var endDate = dateRange.endDate.copy()

        val dayOfMonthList: MutableList<List<String>> = mutableListOf()

        while (ChronoUnit.WEEKS.between(startDate, endDate) > -1) {
            //해당월의 길이
            val monthLength = startDate.lengthOfMonth()

            //해당월의 길이만큼 달력을 구성한다.
            val dayList: MutableList<String> = mutableListOf<String>().apply {
                for (i in 1..monthLength) {
                    //1~마지막 일까지 전부 나타내기엔 너무 길다. 홀수단위로만 나타낸다
                    if (i % 2 == 0) continue
                    add(i.toString())
                }
            }

            dayOfMonthList.add(dayList)
            startDate = startDate.plusMonths(1)
        }

        return dayOfMonthList
    }

    fun getYearList(dateRange: LocalDateRange): List<List<String>> {

        var startDate = dateRange.startDate.copy()
        var endDate = dateRange.endDate.copy()

        val yearList: MutableList<List<String>> = mutableListOf()

        while (ChronoUnit.WEEKS.between(startDate, endDate) > -1) {
            val monthList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")

            yearList.add(monthList)
            startDate = startDate.plusYears(1)
        }

        return yearList
    }

    /**
     * 전달받은 dateRange를 바탕으로 range동안의 1주일의 범위를 전달해준다.
     * */
    fun getWeekRangeList(dateRange: LocalDateRange): List<LocalDateRange> {
        var startDate = dateRange.startDate
        val endDate = dateRange.endDate

        val weekRangeList: MutableList<LocalDateRange> = mutableListOf()

        while (startDate.compareUntilWeek(endDate) <= 0) {
            val weekRange = getWeekDayRangeBy(startDate)

            weekRangeList.add(weekRange)
            startDate = startDate.plusWeeks(1)
        }

        return weekRangeList
    }

    /**
     * 전달받은 dateRange를 바탕으로 range동안의 1달의 범위를 전달해준다.
     * */
    fun getMonthRangeList(dateRange: LocalDateRange): List<LocalDateRange> {
        var startDate = dateRange.startDate
        val endDate = dateRange.endDate

        val monthRangeList: MutableList<LocalDateRange> = mutableListOf()

        while (startDate.compareUntilMonth(endDate) <= 0) {
            val weekRange = getMonthRangeBy(startDate)

            monthRangeList.add(weekRange)
            startDate = startDate.plusMonths(1)
        }

        return monthRangeList
    }

    /**
     * 전달받은 dateRange를 바탕으로 range동안의 1년의 범위를 전달해준다.
     * */
    fun getYearRangeList(dateRange: LocalDateRange): List<LocalDateRange> {
        var startDate = dateRange.startDate
        val endDate = dateRange.endDate

        val yearRangeList: MutableList<LocalDateRange> = mutableListOf()

        while (startDate.compareUntilYear(endDate) <= 0) {
            val weekRange = getYearRangeBy(startDate)

            yearRangeList.add(weekRange)
            startDate = startDate.plusYears(1)
        }

        return yearRangeList
    }

    /**
     * 전달받은 date를 바탕으로 1주일의 범위를 전달해준다.
     * */
    fun getWeekDayRangeBy(date: LocalDate): LocalDateRange {
        val weekNum = date.dayOfWeek.value

        val startDate = date.minusDays(abs(1L - weekNum))
        val endDate = date.plusDays(abs(weekNum - 7L))

        return LocalDateRange(startDate, endDate)
    }

    /**
     * 전달받은 date를 바탕으로 1달의 범위를 전달해준다.
     * */
    fun getMonthRangeBy(date: LocalDate): LocalDateRange {
        val startDate = LocalDate.of(date.year, date.monthValue, 1) //해당 달의 첫날
        val endDate = LocalDate.of(date.year, date.monthValue, date.lengthOfMonth()) //해당 달의 마지막 날

        return LocalDateRange(startDate, endDate)
    }

    /**
     * 전달받은 date를 바탕으로 1년의 범위를 전달해준다.
     * */

    fun getYearRangeBy(date: LocalDate): LocalDateRange {
        val startDate = LocalDate.of(date.year, 1, 1) //해당 년도의 첫날
        val endDate = LocalDate.of(date.year, 12, 31) //해당 년도의 마지막날

        return LocalDateRange(startDate, endDate)
    }

}