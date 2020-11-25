package com.example.todoplusminus.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.databinding.UiColorSelectorBinding
import com.example.todoplusminus.databinding.UiColorSelectorItemBinding
import com.example.todoplusminus.util.ColorManager

class ColorSelectorView : LinearLayout {

    private lateinit var binder : UiColorSelectorBinding
    private var mDelegate : Delegate? = null

    interface Delegate{
        fun onSelect(bgColor : Int)
        fun onDone()
    }

    constructor(context: Context?) : super(context){
        customInit(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        customInit(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        customInit(context)
    }

    fun setDelegate(delegate : Delegate){
        mDelegate = delegate

        (binder.colorList.adapter as? ColorSelectorAdapter)?.setDelegate(delegate)
    }

    private fun customInit(context : Context?){
        binder = UiColorSelectorBinding.inflate(LayoutInflater.from(context), this, true)

        binder.colorList.adapter = ColorSelectorAdapter()

        binder.btnDone.setOnClickListener {
            mDelegate?.onDone()
        }
    }

}

class ColorSelectorAdapter : RecyclerView.Adapter<ColorSelectorAdapter.ColorVH>(){

    private val colorList = ColorManager.colorList
    private var mDelegate : ColorSelectorView.Delegate? = null

    fun setDelegate(delegate: ColorSelectorView.Delegate){
        mDelegate = delegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorVH {
        val vb = UiColorSelectorItemBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return ColorVH(vb)
    }

    override fun getItemCount(): Int = colorList.size

    override fun onBindViewHolder(holder: ColorVH, position: Int) {
        holder.bind()
    }

    inner class ColorVH(private val vb : UiColorSelectorItemBinding) : RecyclerView.ViewHolder(vb.root){

        fun bind(){
            vb.colorItem.setCardBackgroundColor(colorList[adapterPosition])
            vb.colorItem.setOnClickListener {
                mDelegate?.onSelect(colorList[adapterPosition])
            }
        }

    }
}
