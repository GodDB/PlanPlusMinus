package com.example.todoplusminus.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.R
import com.example.todoplusminus.databinding.UiCalendarViewBinding
import com.example.todoplusminus.databinding.UiCalendarViewItemBinding
import com.example.todoplusminus.util.DateHelper
import com.example.todoplusminus.util.DpConverter
import com.example.todoplusminus.util.compareUntilMonth
import java.time.LocalDate

class PMCalendarView : LinearLayout {

    data class CalendarData(
        val date: LocalDate?,
        val isShowCheckView1: Boolean?,
        val isShowCheckView2: Boolean?
    )

    interface Delegate {
        fun selectedDate(date: LocalDate)
    }

    private lateinit var binder: UiCalendarViewBinding
    private var mCalendarDataList: List<List<CalendarData>>? = null
    private var mCalendarTitleList: List<String>? = null
    private var mDelegate: Delegate? = null


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

    fun setCalendarData(calendarList: List<List<CalendarData>>) {
        mCalendarDataList = calendarList.reversed()
        mCalendarTitleList = getCalendarTitleList(mCalendarDataList!!)

        (binder.calendarContentList.adapter as? CalendarViewAdapter)?.setData(mCalendarDataList!!)
        setTitle(mCalendarTitleList!![0])
    }

    fun setSelectDate(date: LocalDate) {
        val monthIndex = getIndexWhenEqualMonth(date)
        val dayIndex = getIndexWhenEqualDay(date, monthIndex)

        if (monthIndex == -1 || dayIndex == -1) return

        //스크롤 이동
        scrollBy(monthIndex)

        //title 변경
        setTitle(mCalendarTitleList?.get(monthIndex))

        //vh 갱신
        val vh = getVHByIndex(monthIndex)
        vh?.setSelectedIndex(dayIndex)

        //어댑터 갱신
        val adapter = binder.calendarContentList.adapter as? CalendarViewAdapter
        adapter?.notifyDataSetChanged()
    }

    private fun getCalendarTitleList(list: List<List<CalendarData>>): List<String> {
        val titleList: MutableList<String> = mutableListOf()
        //한달씩 데이터가 분리되어 있음을 보장한다.
        list.forEach {
            val date = it[(it.size) / 2].date

            val title =
                "${date?.year}${context.getString(R.string.year)} ${date?.monthValue}${context.getString(
                    R.string.month
                )}"
            titleList.add(title)
        }

        return titleList
    }

    private fun scrollBy(index: Int) {
        binder.calendarContentList.scrollToPosition(index)
    }

    private fun customInit(context: Context?) {
        binder = UiCalendarViewBinding.inflate(LayoutInflater.from(context), this, true)

        initCalendarContents()
    }

    private fun initCalendarContents() {

        binder.calendarContentList.adapter = CalendarViewAdapter().apply {
            setDelegate(object : CalendarViewAdapter.Delegate {
                override fun selectedDate(date: LocalDate) {
                    mDelegate?.selectedDate(date)
                }

            })
        }

        binder.calendarContentList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)

        snapHelper.attachToRecyclerView(binder.calendarContentList)
    }

    private fun getIndexWhenEqualMonth(date: LocalDate): Int {
        mCalendarDataList?.forEachIndexed { index, list ->
            val calendarDate = list[list.size / 2].date

            if (calendarDate?.let { date.compareUntilMonth(it) } == 0) return index
        }

        //없으면 -1
        return -1
    }

    private fun getIndexWhenEqualDay(date: LocalDate, monthIndex: Int): Int {
        /*  mCalendarDataList[monthIndex].forEachIndexed { index, value ->
              if(value?.dayOfMonth == date.dayOfMonth) return index
          }*/
        mCalendarDataList?.get(monthIndex)?.forEachIndexed { index, calendarData ->
            if (date.dayOfMonth == calendarData.date?.dayOfMonth) return index
        }

        //없으면 -1
        return -1
    }

    private fun setTitle(title: String?) {
        binder.calendarTitle.text = title
    }

    private fun getVHByIndex(index: Int): CalendarViewAdapter.CalendarVH? {
        val childView =
            (binder.calendarContentList.layoutManager as? LinearLayoutManager)?.findViewByPosition(
                index
            )
        return childView?.let {
            binder.calendarContentList.getChildViewHolder(it)
        } as? CalendarViewAdapter.CalendarVH
    }


    private val snapHelper = object : PagerSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager?,
            velocityX: Int,
            velocityY: Int
        ): Int {
            val index = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)

            setTitle(mCalendarTitleList?.get(index))

            return super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        }
    }
}

class CalendarViewAdapter : RecyclerView.Adapter<CalendarViewAdapter.CalendarVH>() {

    interface Delegate {
        fun selectedDate(date: LocalDate)
    }

    private val mDataList: MutableList<List<PMCalendarView.CalendarData>> = mutableListOf()
    private var mDelegate: Delegate? = null

    fun setData(list: List<List<PMCalendarView.CalendarData>>) {
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
        private val tvList = listOf<TextView>(
            vb.date1, vb.date2, vb.date3, vb.date4, vb.date5, vb.date6, vb.date7, vb.date8,
            vb.date9, vb.date10, vb.date11, vb.date12, vb.date13, vb.date14, vb.date15, vb.date16,
            vb.date17, vb.date18, vb.date19, vb.date20, vb.date21, vb.date22, vb.date23, vb.date24,
            vb.date25, vb.date26, vb.date27, vb.date28, vb.date29, vb.date30, vb.date31, vb.date32,
            vb.date33, vb.date34, vb.date35, vb.date36, vb.date37, vb.date38, vb.date39, vb.date40,
            vb.date41, vb.date42
        )

        private var selectedIndex: Int? = null

        fun setSelectedIndex(index: Int) {
            this.selectedIndex = index
        }

        fun bind() {
            //데이터의 길이만큼 반복한다.
            mDataList[adapterPosition].forEachIndexed { index, calendarData ->
                tvList[index].text = calendarData.date?.dayOfMonth?.toString() ?: ""

                if(calendarData.isShowCheckView1 != null && calendarData.isShowCheckView1) generateCheckView(tvList[index])
                if (calendarData.isShowCheckView2 != null && calendarData.isShowCheckView2) tvList[index].text =
                    tvList[index].context.getString(
                        R.string.memo_icon,
                        tvList[index].text.toString()
                    )

                if (calendarData.date != null) setOnClickEvent(index, calendarData.date)
                else tvList[index].setOnClickListener(null)

                if (selectedIndex != index) tvList[index].setTextColor(Color.BLACK)
                else tvList[index].setTextColor(Color.GREEN)
            }

            setEmptyValueBetween(mDataList[adapterPosition].size, tvList.size)
            clearSelectedIndex()
        }


        private fun clearSelectedIndex() {
            selectedIndex = null
        }

        private fun setOnClickEvent(index: Int, date: LocalDate) {
            tvList[index].setOnClickListener {
                //선택한 일자를 전달한다.
                mDelegate?.selectedDate(date)
            }
        }

        private fun setEmptyValueBetween(from: Int, to: Int) {
            for (i in from until to) tvList[i].text = ""
        }

        /**
         * TextView를 전달받아 textView의 2/3 지점에 조그만한 뷰를 생성한다.
         *
         * 이 뷰는 해당 달력 날짜에 사용자가 plan의 count를 1이라도 올렸으면 표시한다.
         * */
        private fun generateCheckView(tv : TextView){
            tv.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    val x = tv.x
                    val y = tv.y

                    val checkView = CardView(tv.context).apply {
                        val params = ViewGroup.LayoutParams(DpConverter.dpToPx(8f).toInt(), DpConverter.dpToPx(8f).toInt())
                        this.layoutParams = params

                        this.x = x
                        this.y = y
                        this.setCardBackgroundColor(Color.RED)
                    }

                    vb.root.addView(checkView)
                    tv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

    }
}