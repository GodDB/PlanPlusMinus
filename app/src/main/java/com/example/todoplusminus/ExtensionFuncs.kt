package com.example.todoplusminus

import java.time.LocalDate
import java.time.temporal.WeekFields


// ------ LocalDate extension function ---------------------------------

fun LocalDate.copy(): LocalDate =
    LocalDate.of(this.year, this.month, this.dayOfMonth)

/** 
 * LocalDate값을 비교하기 위한 확장함수 
 * 
 * 연도만, 주만, 달만 따로 비교하는 object가 없어서 확장함수 형태로 만든다.
 *
 * target보다 크면 1
 * 작으면 -1
 * 같으면 0
 * */
fun LocalDate.compareUntilWeek(target: LocalDate): Int {

    return when{
        this.compareUntilMonth(target) == 0 -> {
            val thisWeekOfMonth = this.get(WeekFields.ISO.weekOfMonth())
            val targetWeekOfMonth = target.get(WeekFields.ISO.weekOfMonth())
            when{
                thisWeekOfMonth == targetWeekOfMonth -> 0
                thisWeekOfMonth > targetWeekOfMonth -> 1
                else -> -1
            }
        }
        this.compareUntilMonth(target) == 1 -> 1
        else -> -1
    }
}

fun LocalDate.compareUntilMonth(target: LocalDate): Int {
    return when {
        this.year > target.year -> 1
        this.year == target.year -> {
            when {
                this.monthValue > target.monthValue -> 1
                this.monthValue == target.monthValue -> 0
                else -> -1
            }
        }
        else -> -1
    }
}

fun LocalDate.compareUntilYear(target: LocalDate): Int {
    return when {
        this.year > target.year -> 1
        this.year == target.year -> 0
        else -> -1
    }
}

fun LocalDate.customPlusWeeks(value : Long) : LocalDate{
    var date = this.plusWeeks(value)
    //연말 전주에서 1주를 더했을 때 해가 변경된다면 2주가 더해지는 격이다. 그걸 방지하고자 만듬
    if(this.year != date.year){
        val endYear = LocalDate.of(this.year, 12, 31)
        val beforeEndYearOfWeek = endYear.get(WeekFields.ISO.weekOfYear()) -1
        //케이스는 2개가 존재,
        // 연말 전주인데, 1주를 더한다면 해가 넘어감 -> 즉 해가 넘어가지 않고 연말 마지막날로 변경
        // 연말이라서 1주를 더하면 해가 넘어감 -> 그대로 진행
        date = if(beforeEndYearOfWeek == this.get(WeekFields.ISO.weekOfYear())) this.withDayOfMonth(31)
        else this.plusWeeks(1)
    }

    return date
}

// ---------------------------------------------------------------------------------

