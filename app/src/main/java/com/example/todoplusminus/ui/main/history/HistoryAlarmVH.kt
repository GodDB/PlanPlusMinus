package com.example.todoplusminus.ui.main.history

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.ui.setting.ValueData
import com.example.todoplusminus.util.ColorID
import com.example.todoplusminus.util.StringID

abstract class HistoryAlarmVH(itemView : View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data : Triple<List<StringID>, ColorID, ValueData>)
}