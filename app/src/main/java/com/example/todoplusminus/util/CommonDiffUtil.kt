package com.example.todoplusminus.util

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.todoplusminus.entities.PlanData

class CommonDiffUtil(private val oldItems: List<Any>, private val newItems: List<Any>) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        Log.d("godgod", "$oldItemPosition  ${(oldItem as PlanData).index}    |    $newItemPosition ${(newItem as PlanData).index}")
        //todo olditem == newitem으로 처리하면 안되는 이유에 대해서 알아보기

        return if (oldItem is PlanData && newItem is PlanData) oldItem.count == newItem.count
        else if (oldItem is PlanData && newItem is PlanData) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    //data class에 hashcode , equals가 자동 구현되어 있으므로 단순 비교해도 된다.
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return if (oldItem is PlanData && newItem is PlanData) oldItem.count == newItem.count
        else if (oldItem is PlanData && newItem is PlanData) oldItem.id == newItem.id
        else oldItem == newItem
    }


}