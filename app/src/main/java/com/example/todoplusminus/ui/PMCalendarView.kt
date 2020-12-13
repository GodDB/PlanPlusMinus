package com.example.todoplusminus.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.R
import com.example.todoplusminus.databinding.UiCalendarViewBinding
import com.example.todoplusminus.databinding.UiCalendarViewItemBinding
import com.example.todoplusminus.util.DateHelper
import com.example.todoplusminus.util.LocalDateRange
import java.time.LocalDate
import java.util.*

class PMCalendarView : LinearLayout {

    interface Delegate {
        fun selectedDate(year: Int, month: Int, day: Int)
    }

    private lateinit var binder: UiCalendarViewBinding
    private var calendarRange = LocalDateRange(LocalDate.of(2010, 1, 1), LocalDate.now())
    private val calendarDataList = DateHelper().getCalendarBy(calendarRange).reversed()
    private val calendarTitleList = DateHelper().getMonthRangeList(calendarRange).reversed()
    private var mDelegate: Delegate? = null

    //현재 사용자에게 보여지는 년, 월
    private var curDate: LocalDateRange = calendarTitleList[0]

    constructor(context: Context?) : super(context) {
        customInit(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        customInit(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        customInit(context)
    }

    fun setDelegate(delegate: Delegate) {
        mDelegate = delegate
    }

    private fun customInit(context: Context?) {
        binder = UiCalendarViewBinding.inflate(LayoutInflater.from(context), this, true)

        initCalendarContents()
    }

    private fun initCalendarContents() {

        binder.calendarContentList.adapter = CalendarViewAdapter().apply {
            setData(calendarDataList)
            setDelegate(object : CalendarViewAdapter.Delegate{
                override fun selectedDay(day: Int) {
                    mDelegate?.selectedDate(curDate.endDate.year, curDate.endDate.monthValue, day)
                }
            })
        }

        binder.calendarContentList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)

        snapHelper.attachToRecyclerView(binder.calendarContentList)

        binder.calendarTitle.text =
            DateHelper.DateConverter.parseStringMonthRange(
                binder.root.context,
                calendarTitleList[0]
            )
    }

    private val snapHelper = object : PagerSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager?,
            velocityX: Int,
            velocityY: Int
        ): Int {
            val index = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
            binder.calendarTitle.text =
                DateHelper.DateConverter.parseStringMonthRange(
                    binder.root.context,
                    calendarTitleList[index]
                )
            curDate = calendarTitleList[index]

            return super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        }
    }
}

class CalendarViewAdapter : RecyclerView.Adapter<CalendarViewAdapter.CalendarVH>() {

    interface Delegate {
        fun selectedDay(day: Int)
    }

    private val mDataList: MutableList<List<Int>> = mutableListOf()
    private var mDelegate: Delegate? = null

    fun setData(list: List<List<Int>>) {
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    fun setDelegate(delegate: Delegate) {
        mDelegate = delegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarVH {
        val vb =
            UiCalendarViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarVH(vb)
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: CalendarVH, position: Int) {
        holder.bind()
    }


    inner class CalendarVH(private val vb: UiCalendarViewItemBinding) :
        RecyclerView.ViewHolder(vb.root) {
        val tvList = listOf<TextView>(
            vb.date1, vb.date2, vb.date3, vb.date4, vb.date5, vb.date6, vb.date7, vb.date8,
            vb.date9, vb.date10, vb.date11, vb.date12, vb.date13, vb.date14, vb.date15, vb.date16,
            vb.date17, vb.date18, vb.date19, vb.date20, vb.date21, vb.date22, vb.date23, vb.date24,
            vb.date25, vb.date26, vb.date27, vb.date28, vb.date29, vb.date30, vb.date31, vb.date32,
            vb.date33, vb.date34, vb.date35, vb.date36, vb.date37, vb.date38, vb.date39, vb.date40,
            vb.date41, vb.date42
        )

        var selectedIndex: Int? = null

        fun bind() {
            //데이터의 길이만큼 반복한다.
            mDataList[adapterPosition].forEachIndexed { index, value ->
                tvList[index].text = if (value != 0) value.toString() else ""

                if (value != 0) tvList[index].setOnClickListener { tv ->
                    //선택한 일자를 전달한다.
                    mDelegate?.selectedDay(value)

                    //UI 업데이트
                    (tv as? TextView)?.setTextColor(ContextCompat.getColor(tv.context, R.color.pastel_color1))
                    selectedIndex = index
                    notifyDataSetChanged()
                }
                if (selectedIndex != index) tvList[index].setTextColor(Color.BLACK)
            }

            for(i in mDataList[adapterPosition].size until tvList.size) tvList[i].text = ""
        }


    }
}