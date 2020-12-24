package com.example.todoplusminus.bindingAdapter


import android.graphics.Color
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.controllers.PlanHistoryChartAdapter
import com.example.todoplusminus.controllers.PlanListAdapter
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import com.example.todoplusminus.customViews.PMCalendarView
import com.example.todoplusminus.util.*
import com.example.todoplusminus.vm.OnDoneListener
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import kotlin.math.max


/**
 * 리사이클러뷰에 데이터를 바인딩하기 위한 어댑터
 *
 * 앱 특성상 editMode일 때는 리사이클러뷰가 동적으로 변화되는 것이 많아 (추가, 삭제, 이동 등)
 * 전체를 갱신하게끔 처리
 *
 * editMode가 아닐때는 리사이클러뷰의 변경이 count올리는 것 밖에 없으므로, diffutil을 사용하여 변경함.
 * */
@BindingAdapter(value = ["bind:items", "bind:isEdit"], requireAll = true)
fun items(rv: RecyclerView, dataList: List<PlanData>?, isEdit: Boolean) {
    if (rv.adapter == null) return

    if (isEdit) {
        dataList.let {
            (rv.adapter as? PlanListAdapter)?.updateAllItems(it)
        }
    } else {
        dataList.let {
            (rv.adapter as? PlanListAdapter)?.updateDiffItems(it)
        }
    }
}

@BindingAdapter("bind:editMode")
fun setEditMode(view: View, isEdit: Boolean) {
    if (isEdit) return CommonAnimationHelper.startFadeInAnimation(view)

    CommonAnimationHelper.startFadeOutAnimation(view)
}

@BindingAdapter("bind:showCalendar")
fun setShowCalendar(v: View, isShowCalendar: Boolean) {
    if (isShowCalendar) return CommonAnimationHelper.startFadeInAnimation(v)

    CommonAnimationHelper.startFadeOutAnimation(v)
}


@BindingAdapter(value = ["bind:editMode1", "bind:showCalendar"], requireAll = true)
fun setShowCalendar(v: View, isEdit: Boolean, isShowCalendar: Boolean) {
    when {
        isEdit and isShowCalendar -> CommonAnimationHelper.startFadeOutAnimation(v)
        !isEdit and isShowCalendar -> CommonAnimationHelper.startFadeInAnimation(v)
        isEdit and !isShowCalendar -> CommonAnimationHelper.startFadeOutAnimation(v)
        else -> CommonAnimationHelper.startFadeOutAnimation(v)
    }
}

/**
 * y축을 이동시키기 위한 바인딩 어댑터
 *
 * editMode, calendar visible 상황을 반영한다.
 * */
@BindingAdapter(value = ["bind:yMoveWhenEditMode", "bind:yMoveWhenShowCalendar"], requireAll = true)
fun startYMove(viewGroup: ViewGroup, isEdit: Boolean, isShowCalendar: Boolean) {
    when {
        isEdit ->
            CommonAnimationHelper.startYMoveDownAnimation(viewGroup, DpConverter.dpToPx(100f).toInt())
        isShowCalendar ->
            CommonAnimationHelper.startYMoveDownAnimation(viewGroup, DpConverter.dpToPx(50f).toInt())
        else ->
            CommonAnimationHelper.startYMoveDownAnimation(viewGroup, DpConverter.dpToPx(0f).toInt())
    }
}


@BindingAdapter(value = ["bind:backgroundColor", "bind:isEdit"], requireAll = true)
fun setBackgroundColorWhenEditMode(view: CardView, data: PlanData, isEdit: Boolean) {
    if (isEdit)
        view.setCardBackgroundColor(ContextCompat.getColor(view.context, data.bgColor))
    else {
        if (data.count >= 1) view.setCardBackgroundColor(
            ContextCompat.getColor(
                view.context,
                data.bgColor
            )
        )
        else view.setCardBackgroundColor(Color.WHITE)
    }
}


@BindingAdapter("bind:animation")
fun setAnimation(v: View, isEdit: Boolean) {
    if (isEdit)
        CommonAnimationHelper.startVibrateAnimation(v)
    else
        CommonAnimationHelper.stop(v)
}

/**
 * 세로 slide 이벤트를 통해
 * 사용자가 편하게 메모 컨트롤러를 종료시킬 수 있게 해주는 리스너
 * */
@BindingAdapter("bind:setSlideEvent")
fun setSlideEvent(v: View, onComplete: () -> Unit) {
    val verticalSlideEventListener = object : View.OnTouchListener {
        private var firstPressedY: Float = 0f
        private var orgY: Float? = null
        private var maxY: Int = (DeviceManager.getDeviceHeight() * 0.5).toInt()

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    firstPressedY = event.y

                    if (orgY == null) orgY = v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val newY = max(orgY ?: 0f, v.y + event.y - firstPressedY.toInt())
                    v.y = newY
                }

                MotionEvent.ACTION_UP -> {
                    if (v.y > maxY) onComplete()
                    else resetLocation(v)
                }
            }
            return true
        }

        private fun resetLocation(v: View) {
            v.y = this.orgY ?: 0f
        }
    }
    v.setOnTouchListener(verticalSlideEventListener)
}


@BindingAdapter(value = ["bind:setClickEvent", "bind:dim"], requireAll = true)
fun setClickEventAndDim(v: View, listener: OnDoneListener, content: String?) {
    Log.d("godgod", "$content")
    if(content == null ) return

    if (content != "") {
        v.setOnClickListener { listener.onDone() }
        v.alpha = 1f
    } else {
        v.setOnClickListener {}
        v.alpha = 0.2f
    }
}

@BindingAdapter(
    value = ["bind:setGraphViewXData", "bind:setGraphViewYData", "bind:setGraphViewTitle", "bind:setGraphBarColor"],
    requireAll = true
)
fun setGraphViewListData(
    rv: RecyclerView,
    xData: List<List<String>>?,
    yData: List<List<Int>>?,
    titleList: List<LocalDateRange>?,
    graphBarColor: Int?
) {
    if (rv.adapter == null) return
    if (xData == null || yData == null || titleList == null || graphBarColor == null) return

    val mTitleList: List<String> =
        titleList.map { (it.startDate.toString() + " ~ " + it.endDate.toString()) }

    (rv.adapter as PlanHistoryChartAdapter).setData(xData, yData, mTitleList, graphBarColor)
}

@BindingAdapter("bind:tabIndicatorColor")
fun setTabIndicatorColor(tabLayout: TabLayout, bgColor: Int?) {
    if(bgColor == null) return
    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(tabLayout.context, bgColor))
}

@BindingAdapter("bind:setCardViewColorById")
fun setCardViewColorById(view: CardView, colorId: Int) {
    view.setCardBackgroundColor(ContextCompat.getColor(view.context, colorId))
}

@BindingAdapter("bind:setItemClickListener")
fun setOnClickEvent(view: CardView, onClick: () -> Unit) {

    val clickGestureDetector =
        GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
            //todo swipe이벤트와 겹치면 안된다.
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                return super.onSingleTapConfirmed(e)
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean = true
        })

    view.setOnTouchListener { _, event ->
        if (clickGestureDetector.onTouchEvent(event)) onClick()
        true
    }
}

/**
 * planmemo, plandata를 calendar data로 변경시키는 mapper
 *
 * 지정된 날짜 범위 안에서 planmemo와 plandata가 존재하는지를 확인하고, 그에 맞게 calendarData로 변환한다.
 * */
@BindingAdapter(value = ["bind:setCalendarData1", "bind:setCalendarData2"], requireAll = true)
fun setCalendarData(
    calendarView: PMCalendarView,
    planDataList: List<PlanData>?,
    planMemoList: List<PlanMemo>?
) {
    if (planDataList == null || planMemoList == null) return

    //todo 얘를 누가 관리하면 좋을까 ...
    val calendarRange = LocalDateRange(LocalDate.of(2015, 1, 1), LocalDate.now())
    val dateHelper = DateHelper()

    val mCalendarList: MutableList<List<PMCalendarView.CalendarData>> = mutableListOf()

    val sortedPlanDataList = planDataList.sortedBy {
        it.date
    }

    val sortedPlanMemoList = planMemoList.sortedBy {
        it.date
    }

    Log.d("godgod", "${dateHelper.getCalendarBy3(calendarRange)}")

    dateHelper.getCalendarBy3(calendarRange).forEach { dateList ->
        val calendarList: MutableList<PMCalendarView.CalendarData> = mutableListOf()
        dateList.forEach { date ->
            calendarList.add(
                PMCalendarView.CalendarData(
                    date,
                    checkContainPlanDataByDate(sortedPlanDataList, date),
                    checkContainPlanMemoByDate(sortedPlanMemoList, date)
                )
            )
        }
        mCalendarList.add(calendarList)
    }

    calendarView.setCalendarData(mCalendarList)
}

private fun checkContainPlanDataByDate(planDataList: List<PlanData>, date: LocalDate?): Boolean? {
    if (date == null) return null
    planDataList.forEach {
        //planData의 date가 전달받은 date를 넘어가면 종료한다... planData는 날짜 순임을 보장한다.
        if (date.isBefore(it.date)) return false
        if (it.date == date) return true
    }
    return false
}

private fun checkContainPlanMemoByDate(planMemoList: List<PlanMemo>, date: LocalDate?): Boolean? {
    if (date == null) return null
    planMemoList.forEach {
        //planData의 date가 전달받은 date를 넘어가면 종료한다... planData는 날짜 순임을 보장한다.
        if (date.isBefore(it.date)) return false
        if (it.date == date) return true
    }
    return false
}