package com.example.todoplusminus.util

import java.time.LocalDate

/**
 * LocalDate의 범위를 관리하는 object
 * */
class LocalDateRange(val startDate : LocalDate, val endDate : LocalDate){

    /**
     * 전달받은 date가 LocalDataRange안에 포함되는지를 확인한다
     * */
    fun hasContain(date : LocalDate) : Boolean{
        //startDate와 같다.
        if(startDate == date) return true
        //startDate보다 크고, endDate보다 작다.
        else if(startDate.isBefore(date) && endDate.isAfter(date)) return true
        //endDate와 같다.
        else if(endDate == date) return true

        return false
    }
}