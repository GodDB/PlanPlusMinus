package com.example.todoplusminus.ui.main

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.databinding.ControllerPlannerBinding
import com.example.todoplusminus.databinding.ControllerPlannerItemBinding
import com.example.todoplusminus.ui.customViews.PMCalendarView
import com.example.todoplusminus.ui.main.edit.PlanEditController
import com.example.todoplusminus.util.ChangeHandlers.MyTransitionCH
import com.example.todoplusminus.util.CommonDiffUtil
import com.example.todoplusminus.util.VibrateHelper
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.time.LocalDate
import javax.inject.Inject


class MainPlannerController : DBControllerBase {

    interface Delegate {
        fun showMemoEditor()
        fun showHistoryEditor(id: BaseID)
    }

    companion object {
        const val TAG = "planner_controller"
    }


    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(delegate: Delegate) {
        this.mDelegate = delegate
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val planVM by lazy {
        ViewModelProvider(activity as AppCompatActivity, viewModelFactory)
            .get(MainPlannerVM::class.java)
    }
    private lateinit var binder: ControllerPlannerBinding
    private var mDelegate: Delegate? = null

    override fun connectDagger() {
        super.connectDagger()

        (activity?.application as BaseApplication).appComponent.planComponent().create()
            .inject(this)
    }


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_planner, container, false)

        binder.vm = planVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        addEvent()
        onSubscribe()
    }

    override fun onAttach(view: View) {
        binder.subWatch.start()
        binder.mainWatch.start()

        planVM.reload()
    }

    override fun onDetach(view: View) {
        binder.subWatch.stop()
        binder.mainWatch.stop()
    }

    private fun configureUI(){
        configureRV()
    }

    private fun showPlanEditor(id: BaseID) {
        pushController(RouterTransaction.with(
            PlanEditController(id)
        ).apply {
            pushChangeHandler(MyTransitionCH())
            popChangeHandler(MyTransitionCH())
        })
    }


    private fun addEvent() {
        binder.calendarView.setDelegate(
            object : PMCalendarView.Delegate {
                override fun selectedDate(date: LocalDate) {
                    planVM.changeDate(date)
                }
            }
        )
    }


    private fun configureRV() {
        binder.planList.adapter =
            PlanListAdapter(planVM, this)
        binder.planList.layoutManager = LinearLayoutManager(binder.rootView.context)

        itemTouchHelperCallback =
            ItemTouchHelperCallback(binder.planList.adapter as ItemTouchHelperCallback.ItemTouchHelperListener)
        val helper = ItemTouchHelper(itemTouchHelperCallback)
        helper.attachToRecyclerView(binder.planList)
    }

    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback

    private fun onSubscribe() {

        planVM.isEditMode.observe(this, Observer { editMode ->
            itemTouchHelperCallback.enabledLongPress = editMode
            itemTouchHelperCallback.enabledSwipe = !editMode
        })

        planVM.showMemoEditor.observe(this, Observer { event ->
            //livedata 변경 후에 이벤트가 처리된 적 있다면 null, 아니면 변경된 값을 전달받는다.
            event.getContentIfNotHandled()?.let { isShow ->
                if (isShow) mDelegate?.showMemoEditor()
            }
        })

        planVM.showHistoryId.observe(this, Observer { event ->
            //livedata 변경 후에 이벤트가 처리된 적 있다면 null, 아니면 변경된 값을 전달받는다.
            event.getContentIfNotHandled()?.let { id ->
                mDelegate?.showHistoryEditor(id)
            }
        })


        planVM.showEditPlanDataID.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { id ->
                showPlanEditor(id)
            }
        })

        planVM.targetDate.observe(this, Observer { date ->
            binder.calendarView.setSelectDate(date)
        })

        planVM.showFirecrackerAnim.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { show ->
                if (show) showFirecrackerAnim()
            }
        })

    }

    private fun planListScrollMoveTo(index: Int) {
        binder.planList.scrollToPosition(index)
    }

    private fun showFirecrackerAnim() {
        val fireCrackerView = binder.firecrackerAnim

        fireCrackerView.build()
            .addColors(
                getColor(R.color.lt_yellow),
                getColor(R.color.lt_orange),
                getColor(R.color.lt_pink),
                getColor(R.color.dk_cyan),
                getColor(R.color.dk_green)
            )
            .setDirection(0.0, 359.0)
            .setSpeed(0f, 10f)
            .setFadeOutEnabled(true)
            .setTimeToLive(500L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12, 5F))
            .setPosition(
                fireCrackerView.x + fireCrackerView.width / 2,
                fireCrackerView.y + fireCrackerView.height / 5
            )
            .burst(150)
    }

    private fun getColor(colorId: Int) =
        binder.rootView.context.getColor(colorId)

}


/**
 * plan list
 * recyclerView adapter
 * */
class PlanListAdapter(
    private val planVM: MainPlannerVM,
    private val mLifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<PlanListAdapter.PlanListVH>(),
    ItemTouchHelperCallback.ItemTouchHelperListener {

    private val curDataList = mutableListOf<PlanData>()

    private lateinit var binder: ControllerPlannerItemBinding

    fun updateDiffItems(newDatalist: List<PlanData>?) {
        if (newDatalist == null) return

        val callback = CommonDiffUtil(
            curDataList,
            newDatalist
        )
        val result = DiffUtil.calculateDiff(callback)

        this.curDataList.clear()
        this.curDataList.addAll(newDatalist)
        result.dispatchUpdatesTo(this)
    }

    fun updateAllItems(newDatalist: List<PlanData>?) {
        if (newDatalist == null) return

        this.curDataList.clear()
        this.curDataList.addAll(newDatalist)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanListVH {
        binder = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.controller_planner_item,
            parent,
            false
        )
        binder.lifecycleOwner = mLifecycleOwner
        binder.vm = planVM
        return PlanListVH(binder)
    }

    override fun getItemCount(): Int = curDataList.size


    override fun onBindViewHolder(holder: PlanListVH, position: Int) {
        holder.bind()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        //이동할 객체 저장
        val data = curDataList[fromPosition]
        //이동할 객체 삭제
        curDataList.remove(data)
        //이동하고 싶은 position에 추가
        curDataList.add(toPosition, data)

        //adapter에게 데이터 이동을 알린다.
        notifyItemMoved(fromPosition, toPosition)
        //변경이 완료 되었으므로 인덱스를 재 정렬 한다.
        rearrangeIndex()

        //vibrator
        VibrateHelper.start()

        return true
    }

    override fun onSwipe(position: Int, direction: Int) {
        updateCount(position, direction)

        VibrateHelper.start() //바이브레이터
        notifyDataSetChanged()
    }

    private fun updateCount(position: Int, direction: Int) {
        when (direction) {
            ItemTouchHelper.END -> {
                if (checkPlusDirectToRight()) planVM.updateCountByIndex(1, position)
                else planVM.updateCountByIndex(-1, position)
            }
            ItemTouchHelper.START-> {
                if (checkPlusDirectToRight()) planVM.updateCountByIndex(-1, position)
                else planVM.updateCountByIndex(1, position)
            }
        }
    }

    private fun checkPlusDirectToRight(): Boolean =
        AppConfig.swipeDirectionToRight

    /**
     * 인덱스를 재 정렬 한다.
     * vm이 지닌 livedata의 list와
     * 어댑터가 지닌 list가 같은 주소값을 지니고 있으므로
     * curList의 index를 변경하면 vm의 index도 변경된다.
     */
    private fun rearrangeIndex() {
        curDataList.reversed().forEachIndexed { index, planData ->
            planData.index = index
        }
    }


    inner class PlanListVH(private val binder: ControllerPlannerItemBinding) :
        RecyclerView.ViewHolder(binder.root) {

        fun bind() {
            binder.index = adapterPosition
            binder.executePendingBindings()
        }
    }
}


/**
 * recyclerView item move, swipe 이벤트를 제공하는 object
 * */
class ItemTouchHelperCallback(private val listener: ItemTouchHelperListener) :
    ItemTouchHelper.Callback() {

    interface ItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
        fun onSwipe(position: Int, direction: Int)
    }

    var enabledLongPress = false
    var enabledSwipe = true

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean = enabledLongPress
    override fun isItemViewSwipeEnabled(): Boolean = enabledSwipe

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwipe(viewHolder.adapterPosition, direction)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.00005f
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            super.onChildDraw(c, recyclerView, viewHolder, dX/5, dY, actionState, isCurrentlyActive)
        }

    }

}