package com.example.todoplusminus.ui.customViews

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.R
import com.example.todoplusminus.databinding.UiCalendarViewBinding
import com.example.todoplusminus.databinding.UiCalendarViewItemBinding
import com.example.todoplusminus.util.DateHelper
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
    private var mSelectedDate : LocalDate = DateHelper.getCurDate()


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

        setSelectDate(mSelectedDate)
    }

    fun setSelectDate(date: LocalDate) {
        val weekIndex = getIndexWhenEqualWeek(date)

        if (weekIndex == -1) return

        //스크롤 이동
        scrollBy(weekIndex)

        //title 변경
        setTitle(mCalendarTitleList?.get(weekIndex))

        //어댑터 갱신
        binder.calendarContentList.recycledViewPool.clear()
        val adapter = binder.calendarContentList.adapter as? CalendarViewAdapter
        adapter?.setSelectedDate(date)

        mSelectedDate = date
    }

    fun setFont(font : Typeface?){
        if(font == null) return

        binder.calendarTitle.typeface = font
        binder.monday.typeface = font
        binder.tuesday.typeface = font
        binder.wednesday.typeface = font
        binder.thursday.typeface = font
        binder.friday.typeface = font
        binder.saturday.typeface = font
        binder.sunday.typeface = font

        (binder.calendarContentList.adapter as? CalendarViewAdapter)?.setFont(font)
    }

    private fun getCalendarTitleList(list: List<List<CalendarData>>): List<String> {
        val titleList: MutableList<String> = mutableListOf()
        list.forEach {
            val date = it[0].date

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

        binder.calendarContentList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)

        snapHelper.attachToRecyclerView(binder.calendarContentList)
    }

    private fun getIndexWhenEqualWeek(date: LocalDate): Int {
        mCalendarDataList?.forEachIndexed { index, list ->
            list.forEach {
                if(date == it.date) return index
            }
        }

        //없으면 -1
        return -1
    }

    private fun setTitle(title: String?) {
        binder.calendarTitle.text = title
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
    private var mSelectedDate: LocalDate? = null
    private var font : Typeface? = null

    fun setData(list: List<List<PMCalendarView.CalendarData>>) {
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    fun setDelegate(delegate: Delegate) {
        mDelegate = delegate
    }

    fun setSelectedDate(date: LocalDate) {
        this.mSelectedDate = date
        notifyDataSetChanged()
    }

    fun setFont(font : Typeface){
        this.font = font
        notifyDataSetChanged()
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

        private val todayDate = DateHelper.getCurDate()

        private val tvList = listOf(
            vb.date1, vb.date2, vb.date3, vb.date4, vb.date5, vb.date6, vb.date7
        )

        private val checkViewList = listOf(
            vb.date1CheckView,
            vb.date2CheckView,
            vb.date3CheckView,
            vb.date4CheckView,
            vb.date5CheckView,
            vb.date6CheckView,
            vb.date7CheckView
        )

        private val selectedViewList = listOf(
            vb.date1Selected,
            vb.date2Selected,
            vb.date3Selected,
            vb.date4Selected,
            vb.date5Selected,
            vb.date6Selected,
            vb.date7Selected
        )

        fun bind() {
            if(adapterPosition == RecyclerView.NO_POSITION) {
                Log.d("godgod", "no")
                return
            }
            //데이터의 길이만큼 반복한다.
            mDataList[adapterPosition].forEachIndexed { index, calendarData ->
                tvList[index].text = calendarData.date?.dayOfMonth?.toString() ?: ""
                tvList[index].typeface = font

                // 체크뷰 보여줄 것인가?
                if (calendarData.isShowCheckView1 == true) checkViewList[index].visibility =
                        View.VISIBLE
                else checkViewList[index].visibility = View.GONE

                // 체크뷰2 보여줄 것인가?
                if (calendarData.isShowCheckView2 == true) tvList[index].text =
                    tvList[index].context.getString(
                        R.string.memo_icon,
                        tvList[index].text.toString()
                    )

                // date값이 존재
                if (calendarData.date != null) setOnClickEvent(index, calendarData.date)
                else tvList[index].setOnClickListener(null)

                // 사용자가 선택한 날짜
                if (mSelectedDate != calendarData.date) selectedViewList[index].visibility = View.GONE
                else selectedViewList[index].visibility = View.VISIBLE

                // 오늘 날짜
                if (todayDate == calendarData.date) tvList[index].setTextColor(
                    ContextCompat.getColor(
                        vb.root.context,
                        R.color.pastel_color1
                    )
                )
                else tvList[index].setTextColor(Color.BLACK)

            }
        }


        private fun setOnClickEvent(index: Int, date: LocalDate) {
            tvList[index].setOnClickListener {
                //선택한 일자를 전달한다.
                mDelegate?.selectedDate(date)
            }
        }
    }
}