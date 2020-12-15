package com.example.todoplusminus.util

import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*


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
            val thisWeekOfMonth = this.get(WeekFields.of(Locale.FRANCE).weekOfMonth()) // 프랑스는 1주를 월~일이다.
            val targetWeekOfMonth = target.get(WeekFields.of(Locale.FRANCE).weekOfMonth())
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

// ---------------------------------------------------------------------------------

fun CardView.setColorById(id : Int){
    this.setCardBackgroundColor(this.context.getColor(id))
}
