package com.example.todoplusminus.bindingAdapter


import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.controllers.PlanListAdapter
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.ui.CreatePlanView
import com.example.todoplusminus.util.CommonAnimationHelper
import com.example.todoplusminus.util.DpConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * 리사이클러뷰에 데이터를 바인딩하기 위한 어댑터
 *
 * 앱 특성상 editMode일 때는 리사이클러뷰가 동적으로 변화되는 것이 많아 (추가, 삭제, 이동 등)
 * 전체를 갱신하게끔 처리
 *
 * editMode가 아닐때는 리사이클러뷰의 변경이 count올리는 것 밖에 없으므로, diffutil을 사용하여 변경함.
 * */
@BindingAdapter(value = ["bind:items", "bind:isEdit"], requireAll = true)
fun items(rv: RecyclerView, dataList: LiveData<List<PlanData>>, isEdit: Boolean) {
    if (rv.adapter == null) return

    if(isEdit){
        dataList.value?.let {
            (rv.adapter as? PlanListAdapter)?.updateAllItems(it)
        }
    }
    else{
        dataList.value?.let {
            (rv.adapter as? PlanListAdapter)?.updateDiffItems(it)
        }
    }
}

@BindingAdapter("bind:editMode")
fun setEditMode(view: View, isEdit: Boolean) {
    CommonAnimationHelper.startFadeAnimation(view, isEdit)
}

/**
 * 리사이클러뷰만의 editMode
 *
 * editMode시에 createPlanView의 height만큼 내린다
 * */
@BindingAdapter("bind:editMode")
fun setEditMode(rv: RecyclerView, isEdit: Boolean) {
    if (isEdit){
        val marginParam = (rv.layoutParams as ViewGroup.MarginLayoutParams)
        marginParam.topMargin = DpConverter.dpToPx(100f).toInt()
        rv.layoutParams = marginParam
    }
    else{
        val marginParam = (rv.layoutParams as ViewGroup.MarginLayoutParams)
        marginParam.topMargin = 0
        rv.layoutParams = marginParam
    }

}

@BindingAdapter(value = ["bind:backgroundColor", "bind:isEdit"], requireAll = true)
fun setBackgroundColor(view: CardView, data: PlanData, isEdit : Boolean) {
    if(isEdit)
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




