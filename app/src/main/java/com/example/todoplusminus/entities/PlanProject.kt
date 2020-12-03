package com.example.todoplusminus.entities

/**
 * 모든 플랜을 관리하는 planObject
 *
 * 하나의 planList를 관리하며, 그외 데이터와 밀접한 비즈니스 로직을 처리한다.
 * */
class PlanProject {

    companion object {
        fun create(plandataList: List<PlanData>?): PlanProject =
            PlanProject().apply {
                setPlanDatas(plandataList ?: mutableListOf(PlanData.create()))
            }
    }

    private var mPlanDataList: MutableList<PlanData> = mutableListOf(PlanData.create())

    fun setPlanDatas(planList: List<PlanData>) {
        mPlanDataList.clear()
        mPlanDataList.addAll(planList)
    }

    fun addPlanData(planData: PlanData) {
        mPlanDataList.add(planData)
    }

    fun getPlanDataByIndex(index: Int) = mPlanDataList[index]

    fun getPlanDataList() = mPlanDataList.toList()

    fun getPlanDataById(id: String): PlanData {
        mPlanDataList.forEach { planData ->
            if (planData.id == id) return planData
        }
        return PlanData.create()
    }

    fun incrementPlanDataCountByIndex(count: Int, index: Int) {
        mPlanDataList[index].incrementCount(count)
    }

    fun getPlanDataBgColorByIndex(index: Int) = mPlanDataList[index].bgColor
    fun getPlanDataIdByIndex(index: Int) = mPlanDataList[index].id
    fun getPlanDataTitleByIndex(index: Int) = mPlanDataList[index].title
    fun getPlanDataCountByIndex(index: Int) = mPlanDataList[index].count

    //todo calculate logic
}