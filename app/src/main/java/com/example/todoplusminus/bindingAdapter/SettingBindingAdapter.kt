package com.example.todoplusminus.bindingAdapter

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.ui.setting.fontSetting.FontListAdapter
import com.example.todoplusminus.ui.setting.SettingListAdapter
import com.example.todoplusminus.ui.setting.fontSetting.FontItemData
import com.example.todoplusminus.ui.setting.SettingData


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

