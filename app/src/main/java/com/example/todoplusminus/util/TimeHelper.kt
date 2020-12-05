package com.example.todoplusminus.util

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

object TimeHelper {

    fun getCurAllDate() : String{
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return result.format(date)
    }


    fun getCurDate() : LocalDate = LocalDate.now()

    fun getCurDateTime() : String{
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("MM/dd\n a HH:mm")
        return result.format(date)
    }

    fun getCurTime() : String{
        val curMills = System.currentTimeMillis()
        val date = Date(curMills)
        val result = SimpleDateFormat("HH:mm:ss")
        return result.format(date)
    }


    /**
     * 오늘 날짜가 포함된 1주일 날짜 구하기
     *
     * ex : 12월 3일 금요일이라면,  11월29일 ,30일, 12월 1일, 2일, 3일, 4일, 5일이 출력값으로 나타난다.
     * */

}