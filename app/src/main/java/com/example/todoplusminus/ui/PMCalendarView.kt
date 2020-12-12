package com.example.todoplusminus.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.databinding.UiCalendarViewBinding
import com.example.todoplusminus.databinding.UiCalendarViewItemBinding
import java.util.*


// https://github.com/WoochanLee/Android-BaseCalendar/blob/master/app/src/main/java/com/github/woochanlee/basecalendar/BaseCalendar.kt
class PMCalendarView : LinearLayout {

    private lateinit var binder : UiCalendarViewBinding

    private val monthData : MutableList<Int> = mutableListOf()
    private val mCalendar : Calendar = Calendar.getInstance()

    constructor(context: Context?) : super(context){
        customInit(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        customInit(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        customInit(context)
    }

    private fun customInit(context: Context?){
        binder = UiCalendarViewBinding.inflate(LayoutInflater.from(context), this, true)

        binder.calendarContentList.adapter = CalendarViewAdapter()
        binder.calendarContentList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        makeMonthDate()
    }

    fun changeToPrevMonth(){
        if(mCalendar.get(Calendar.MONTH)  == 0){
            mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) - 1)
            mCalendar.set(Calendar.MONTH, Calendar.DECEMBER)
        }
        else {
            mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) - 1)
        }
        makeMonthDate()
    }

    fun changeToNextMonth(){
        if(mCalendar.get(Calendar.MONTH) == Calendar.DECEMBER){
            mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) +1)
            mCalendar.set(Calendar.MONTH, 0)
        }
        else{
            mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) + 1)
        }
        makeMonthDate()
    }

    private fun makeMonthDate(){
        monthData.clear()

        mCalendar.set(Calendar.DATE, 1)

        val prevMonthTail = mCalendar.get(Calendar.DAY_OF_WEEK) -2
        makePrevMonthTail(mCalendar.clone() as Calendar, prevMonthTail)
        makeCurrentMonth(mCalendar)

        (binder.calendarContentList.adapter as CalendarViewAdapter).setData(monthData)
    }

    private fun makePrevMonthTail(calendar : Calendar, prevMonthTail : Int){
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        var maxOffsetDate = maxDate - prevMonthTail //달력은 월요일부터 나타내기에 ... +1을 하지 않으면 일요일에 나타낼 달력에 맞다.

        for (i in 1..prevMonthTail) monthData.add(++maxOffsetDate)
    }

    private fun makeCurrentMonth(calendar : Calendar){
        for(i in 1.. calendar.getActualMaximum(Calendar.DATE)) monthData.add(i)
    }
}

class CalendarViewAdapter : RecyclerView.Adapter<CalendarViewAdapter.CalendarVH>(){

    private val mDataList : MutableList<Int> = mutableListOf()

    fun setData(list : List<Int>){
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarVH {
        val vb = UiCalendarViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarVH(vb)
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: CalendarVH, position: Int) {
        holder.bind()
    }


    inner class CalendarVH(private val vb : UiCalendarViewItemBinding) : RecyclerView.ViewHolder(vb.root){
        fun bind(){
            //todo 깔끔하게 리스트로 관리하
            vb.date1.text = mDataList[0].toString()
            vb.date2.text = mDataList[1].toString()
            vb.date3.text = mDataList[2].toString()
            vb.date4.text = mDataList[3].toString()
            vb.date5.text = mDataList[4].toString()
            vb.date6.text = mDataList[5].toString()
            vb.date7.text = mDataList[6].toString()
            vb.date8.text = mDataList[7].toString()
            vb.date9.text = mDataList[8].toString()
            vb.date10.text = mDataList[9].toString()
            vb.date11.text = mDataList[10].toString()
            vb.date12.text = mDataList[11].toString()
            vb.date13.text = mDataList[12].toString()
            vb.date14.text = mDataList[13].toString()
            vb.date15.text = mDataList[14].toString()
            vb.date16.text = mDataList[15].toString()
            vb.date17.text = mDataList[16].toString()
            vb.date18.text = mDataList[17].toString()
            vb.date19.text = mDataList[18].toString()
            vb.date20.text = mDataList[19].toString()
            vb.date21.text = mDataList[20].toString()


        }
    }
}