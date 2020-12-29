package com.example.todoplusminus.ui.customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.databinding.UiTitleSelectorBinding
import com.example.todoplusminus.databinding.UiTitleSelectorItemBinding
import com.example.todoplusminus.util.TitleManager
import com.example.todoplusminus.ui.main.edit.PlanEditVM

class TitleSelectorView : LinearLayout {

    private lateinit var binder: UiTitleSelectorBinding

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

    fun setVM(vm: PlanEditVM) {
        (binder.titleList.adapter as? TitleSelectorAdapter)?.setVM(vm)
    }

    private fun customInit(context: Context?) {
        binder = UiTitleSelectorBinding.inflate(LayoutInflater.from(context), this, true)

        binder.titleList.adapter = TitleSelectorAdapter()
        binder.titleList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}

class TitleSelectorAdapter : RecyclerView.Adapter<TitleSelectorAdapter.TitleVH>() {

    private val titleList = TitleManager.titleIdList
    private var mVM: PlanEditVM? = null
    private var font : Typeface? = null

    fun setVM(vm: PlanEditVM) {
        mVM = vm
        notifyDataSetChanged()
    }

    fun setFont(font : Typeface){
        this.font = font
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleVH {
        val vb =
            UiTitleSelectorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TitleVH(vb)
    }

    override fun getItemCount(): Int = titleList.size

    override fun onBindViewHolder(holder: TitleVH, position: Int) {
        holder.bind()
    }

    inner class TitleVH(private val vb: UiTitleSelectorItemBinding) :
        RecyclerView.ViewHolder(vb.root) {

        fun bind() {
            vb.title = vb.root.context.getString(titleList[adapterPosition])
            vb.vm = mVM
        }
    }

}

