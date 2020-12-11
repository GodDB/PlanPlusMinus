package com.example.todoplusminus.bindingAdapter


import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.controllers.PlanHistoryChartAdapter
import com.example.todoplusminus.controllers.PlanListAdapter
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.util.CommonAnimationHelper
import com.example.todoplusminus.util.DeviceManager
import com.example.todoplusminus.util.DpConverter
import com.example.todoplusminus.util.LocalDateRange
import com.example.todoplusminus.vm.OnDoneListener
import com.google.android.material.tabs.TabLayout
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
    if(isEdit) return CommonAnimationHelper.startFadeInAnimation(view)

    CommonAnimationHelper.startFadeOutAnimation(view)
}

@BindingAdapter("bind:showCalendar")
fun setShowCalendar(v : View, isShowCalendar: Boolean){
    if(isShowCalendar) return CommonAnimationHelper.startFadeInAnimation(v)

    CommonAnimationHelper.startFadeOutAnimation(v)
}

@BindingAdapter("bind:yMoveWhenshowCalendar")
fun setYMoveWhenShowCalendar(v : View, isShowCalendar: Boolean){
    if(isShowCalendar) return CommonAnimationHelper.startYMoveUpAnimation(v, DpConverter.dpToPx(30f).toInt())

    CommonAnimationHelper.startYMoveUpAnimation(v, 0)
}


@BindingAdapter(value = ["bind:editMode1", "bind:showCalendar"], requireAll = true)
fun setShowCalendar(v: View, isEdit: Boolean, isShowCalendar: Boolean) {
    when{
        isEdit and isShowCalendar -> CommonAnimationHelper.startFadeOutAnimation(v)
        !isEdit and isShowCalendar -> CommonAnimationHelper.startFadeInAnimation(v)
        isEdit and !isShowCalendar -> CommonAnimationHelper.startFadeOutAnimation(v)
        else -> CommonAnimationHelper.startFadeOutAnimation(v)
    }
}
/**
 * 리사이클러뷰만의 editMode
 *
 * editMode시에 createPlanView의 height만큼 내린다
 * */
@BindingAdapter(value = ["bind:editMode", "bind:showCalendar"], requireAll = true)
fun setShowCalendar(rv: RecyclerView, isEdit: Boolean, isShowCalendar: Boolean) {
    when {
        isEdit ->
            CommonAnimationHelper.startYMoveDownAnimation(rv, DpConverter.dpToPx(100f).toInt())
        isShowCalendar ->
            CommonAnimationHelper.startYMoveDownAnimation(rv, DpConverter.dpToPx(250f).toInt())
        else ->
            CommonAnimationHelper.startYMoveDownAnimation(rv, DpConverter.dpToPx(0f).toInt())
    }
}


@BindingAdapter(value = ["bind:backgroundColor", "bind:isEdit"], requireAll = true)
fun setBackgroundColorWhenEditMode(view: CardView, data: PlanData, isEdit: Boolean) {
    if (isEdit)
        view.setCardBackgroundColor(data.bgColor)
    else {
        if (data.count >= 1) view.setCardBackgroundColor(data.bgColor)
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
fun setClickEventAndDim(v: View, listener: OnDoneListener, content: String) {
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
    xData: List<List<String>>,
    yData: List<List<Int>>,
    titleList: List<LocalDateRange>,
    graphBarColor: Int
) {
    if (rv.adapter == null) return

    val mTitleList: List<String> =
        titleList.map { (it.startDate.toString() + " ~ " + it.endDate.toString()) }

    (rv.adapter as PlanHistoryChartAdapter).setData(xData, yData, mTitleList, graphBarColor)
}

@BindingAdapter("bind:tabIndicatorColor")
fun setTabIndicatorColor(tabLayout: TabLayout, bgColor: Int) {
    tabLayout.setSelectedTabIndicatorColor(bgColor)
}

@BindingAdapter("bind:setCardViewColorById")
fun setCardViewColorById(view: CardView, colorId: Int) {
    view.setCardBackgroundColor(view.context.getColor(colorId))
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
