package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.MyTransitionCH
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlannerBinding
import com.example.todoplusminus.databinding.PlanListItemBinding
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.ui.PMCalendarView
import com.example.todoplusminus.util.*
import com.example.todoplusminus.vm.PlanEditVM
import com.example.todoplusminus.vm.PlannerViewModel
import com.example.todoplusminus.vm.PlannerVMFactory
import kotlin.math.max
import kotlin.math.min


class PlannerController : DBControllerBase {

    interface Delegate {
        fun showMemoEditor()
        fun showHistoryEditor(id: String)
    }

    companion object {
        const val TAG = "planner_controller"
    }

    private lateinit var binder: ControllerPlannerBinding
    private lateinit var planVM: PlannerViewModel
    private var mDelegate: Delegate? = null

    private var repository: PlannerRepository? = null

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(repository: PlannerRepository, delegate: Delegate?) {
        this.repository = repository
        mDelegate = delegate
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_planner, container, false)

        val factory = PlannerVMFactory(repository!!)
        planVM = ViewModelProvider(
            activity as AppCompatActivity,
            factory
        ).get(PlannerViewModel::class.java)

        binder.vm = planVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        Log.d("godgod", "onCreate")
        binder.subWatch.start()
        binder.mainWatch.startAnimation()

        addEvent()
        configureRV()
        onSubscribe()
    }

    private fun showPlanEditor(id: String) {
        //todo test
        val factory = PlannerVMFactory(repository!!)
        val vm =
            ViewModelProvider(activity as AppCompatActivity, factory).get(PlanEditVM::class.java)

        pushController(RouterTransaction.with(
            PlanEditController(vm.apply {
                setData(id)
            })
        ).apply {
            //todo view 살리는 방법으로 transition 구현해보자 ...
            pushChangeHandler(MyTransitionCH())
            popChangeHandler(MyTransitionCH())
        })
    }


    private fun addEvent() {
        binder.calendarView.setDelegate(
            object : PMCalendarView.Delegate{
                override fun selectedDate(year: Int, month: Int, day: Int) {
                    planVM.changeDate(year, month, day)
                }
            }
        )
    }

    private fun configureRV() {
        binder.planList.adapter = PlanListAdapter(planVM)
        binder.planList.layoutManager = LinearLayoutManager(activity!!)

        itemTouchHelperCallback =
            ItemTouchHelperCallback(binder.planList.adapter as PlanListAdapter)
        val helper = ItemTouchHelper(itemTouchHelperCallback)
        helper.attachToRecyclerView(binder.planList)

        itemSwipeEventHelper.attachToRecyclerView(binder.planList)
    }

    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback

    private fun onSubscribe() {

        planVM.isEditMode.observe(this, Observer { editMode ->
            itemTouchHelperCallback.enabledLongPress = editMode
            itemSwipeEventHelper.isSwipeEnabled = !editMode
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
    }

    private fun planListScrollMoveTo(index: Int) {
        binder.planList.scrollToPosition(index)
    }

    /**
     * 리사이클러뷰의 swipe 이벤트를 담당하는 object
     *
     * 다른 곳에서 재활용 할 일이 없으므로, 익명객체로 생성한다.
     * editMode에선 작동하지 않는다.
     * */
    private val itemSwipeEventHelper = object {
        var isSwipeEnabled = true

        private var mRecyclerView: RecyclerView? = null
        private var mLayoutManager: RecyclerView.LayoutManager? = null

        //swipe로 이동할 수 있는 view의 x좌표
        private val minLeft = -DpConverter.dpToPx(70f)
        private val maxRight
            get() = DeviceManager.getDeviceWidth() + DpConverter.dpToPx(70f) - viewWidth!!

        //뷰의 원래 위치
        private var viewWidth: Int? = null
        private val originX
            get() = DeviceManager.getDeviceWidth() / 2 - (viewWidth?.div(2) ?: 0)

        //사용자가 처음 눌렀을 때의 위치
        private var firstPressedX = 0f

        private var result = true

        fun attachToRecyclerView(rv: RecyclerView?) {
            mRecyclerView = rv
            mLayoutManager = rv?.layoutManager
            mRecyclerView?.addOnItemTouchListener(mItemTouchListener)
        }

        private val mItemTouchListener = object : RecyclerView.OnItemTouchListener {
            private var targetView: View? = null

            override fun onTouchEvent(rv: RecyclerView, event: MotionEvent) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                if (isSwipeEnabled)
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            firstPressedX = event.x
                            targetView = rv.findChildViewUnder(event.x, event.y)
                            viewWidth = targetView?.width
                        }

                        MotionEvent.ACTION_MOVE -> {
                            if (result) {
                                targetView?.let { v ->
                                    v.x = min(
                                        maxRight,
                                        max(originX + event.x - firstPressedX, minLeft)
                                    )

                                    //왼쪽 swipe로 끝지점에 도달
                                    if (v.x == minLeft) {
                                        resetLocation(v)
                                        VibrateHelper.start()
                                        result = false

                                        val position = mLayoutManager?.getPosition(v)
                                        if (position != null) {
                                            planVM.updateCountByIndex(-1, position)
                                        }
                                    }

                                    //오른쪽 swipe로 끝지점에 도달
                                    else if (v.x == maxRight) {
                                        resetLocation(v)
                                        VibrateHelper.start()
                                        result = false

                                        val position = mLayoutManager?.getPosition(v)
                                        if (position != null) {
                                            planVM.updateCountByIndex(1, position)
                                        }
                                    }
                                }
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            targetView?.let { v ->
                                resetLocation(v)
                            }
                            result = true
                        }
                    }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

        // 뷰의 위치를 원래대로 복귀시킨다.
        private fun resetLocation(v: View) {
            v.x = originX
        }
    }
}


/**
 * plan list
 * recyclerView adapter
 * */
class PlanListAdapter(private val planVM: PlannerViewModel) :
    RecyclerView.Adapter<PlanListAdapter.PlanListVH>(),
    ItemTouchHelperCallback.ItemTouchHelperListener {

    private val curDataList = mutableListOf<PlanData>()

    private lateinit var binder: PlanListItemBinding

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
            R.layout.plan_list_item,
            parent,
            false
        )
        return PlanListVH(binder)
    }

    override fun getItemCount(): Int = curDataList.size


    override fun onBindViewHolder(holder: PlanListVH, position: Int) {
        holder.bind(planVM)
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
        //직접 구현해서 사용함.
    }


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

    inner class PlanListVH(private val binder: PlanListItemBinding) :
        RecyclerView.ViewHolder(binder.root) {

        fun bind(planVM: PlannerViewModel) {
            binder.vm = planVM
            binder.index = adapterPosition
            binder.executePendingBindings()

        }
    }
}


/**
 * recyclerView item들을 이동시켜주는 object
 * */
class ItemTouchHelperCallback(private val listener: ItemTouchHelperListener) :
    ItemTouchHelper.Callback() {

    interface ItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
        fun onSwipe(position: Int, direction: Int)
    }


    var enabledLongPress = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean = enabledLongPress
    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

}