package com.example.todoplusminus.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.databinding.UiTitleSelectorBinding
import com.example.todoplusminus.databinding.UiTitleSelectorItemBinding
import com.example.todoplusminus.util.TitleManager

class TitleSelectorView : LinearLayout {

    private lateinit var binder: UiTitleSelectorBinding
    private var mDelegate: Delegate? = null

    interface Delegate {
        fun onSelect(title: String)
    }

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

        (binder.titleList.adapter as? TitleSelectorAdapter)?.setDelegate(delegate)
    }

    private fun customInit(context: Context?) {
        binder = UiTitleSelectorBinding.inflate(LayoutInflater.from(context), this, true)

        binder.titleList.adapter = TitleSelectorAdapter()
        binder.titleList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}

class TitleSelectorAdapter : RecyclerView.Adapter<TitleSelectorAdapter.TitleVH>() {

    private val titleList = TitleManager.titleList
    private var mDelegate: TitleSelectorView.Delegate? = null

    fun setDelegate(delegate: TitleSelectorView.Delegate) {
        mDelegate = delegate
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
            vb.titleItem.text = titleList[adapterPosition]
            vb.root.setOnClickListener {
                mDelegate?.onSelect(titleList[adapterPosition])
            }
        }

    }


}