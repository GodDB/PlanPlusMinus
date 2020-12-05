package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.repository.PlannerRepository

class PlanHistoryContentVM(_mode : String, private val repository: PlannerRepository) {

    val mode : LiveData<String> = MutableLiveData(_mode)

    companion object{
        const val MODE_WEEK = "week_mode"
        const val MODE_MONTH = "month_mode"
        const val MODE_YEAR = "year_mode"
    }
}