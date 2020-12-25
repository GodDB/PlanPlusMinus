package com.example.todoplusminus.bindingAdapter

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.controllers.FontListAdapter
import com.example.todoplusminus.controllers.SettingListAdapter
import com.example.todoplusminus.vm.FontItemData
import com.example.todoplusminus.vm.SettingData
import com.example.todoplusminus.vm.ValueString


// -- setting vm

@BindingAdapter("bind:setSettingItems")
fun setSettingItems(rv : RecyclerView, itemList : List<Pair<Int, SettingData>>?){
    Log.d("godgod", "${itemList == null}   ${AppConfig.fontName}")
    if(itemList == null) return

    val adapter = (rv.adapter as? SettingListAdapter) ?: return
    adapter.setSettingDatas(itemList)
}


// -- fontsetting vm

@BindingAdapter("setFontListItems")
fun setFontListItems(rv : RecyclerView, itemList: List<FontItemData>?){
    if(itemList == null) return

    val adapter = (rv.adapter as? FontListAdapter) ?: return
    adapter.setDataList(itemList)
}

