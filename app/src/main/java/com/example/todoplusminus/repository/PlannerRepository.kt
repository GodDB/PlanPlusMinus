package com.example.todoplusminus.repository

import androidx.lifecycle.LiveData
import com.example.todoplusminus.db.PlannerInfoEntity
import com.example.todoplusminus.db.PlannerItemEntity
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import java.time.LocalDate

class PlannerRepository(
    private val localSource: ILocalDataSource
) {

    /**
     * initialize function
     * */
    fun getAllPlannerData(): LiveData<MutableList<PlanData>> = localSource.getAllPlannerData()

    suspend fun getAllPlanDataByDate(date: LocalDate): LiveData<MutableList<PlanData>> {

        //전달 받은 날짜에 해당하는 info데이터가 있는지 확인한다.
        //즉 전달받은 낧짜에 info 데이터가 없다는 것은 2가지를 의미한다.

        //1. 사용자가 첫 설치해서, 등록한 데이터가 없는 경우
        //2. 새로운 날짜가 되어, 그에 대응되는 info 테이블이 없는 경우
        if(!checkWhetherExistInfoDataByDate(date)){

            //2번째 케이스에 대응된다.
            val itemList = getAllPlanItem()
            if(itemList.isNotEmpty()){
                itemList.forEach {
                   val info = generateInfoData(it.id, date)
                    localSource.insertPlanInfo(info)
                }
            }
        }

        return localSource.getAllPlannerDataByDate(date)
    }

    fun getAllPlanDataById(id: String): List<PlanData> =
        localSource.getAllPlannerDataById(id)

    fun getLastIndex(): Int = localSource.getLastIndex()

    fun getMemoByDate(date: LocalDate): LiveData<PlanMemo> = localSource.getMemoByDate(date)

    suspend fun deletePlannerDataById(id: String) {
        localSource.deletePlannerDataById(id)
    }

    suspend fun insertPlannerData(planData: PlanData) {
        localSource.insertPlannerData(planData)
    }

    suspend fun updatePlannerDataList(dataList: List<PlanData>) {
        localSource.updatePlannerDataList(dataList)
    }

    suspend fun updatePlannerData(data: PlanData) {
        localSource.updatePlannerData(data)
    }

    suspend fun getPlannerDataById(id: String) = localSource.getPlannerDataById(id)

    suspend fun deleteAndUpdateAll(deleteTarget: PlanData, updateTarget: List<PlanData>) {
        localSource.deleteAndUpdateAll(deleteTarget, updateTarget)
    }

    suspend fun updateTitleBgById(id: String, title: String, bgColor: Int) {
        localSource.updateTitleBgById(id, title, bgColor)
    }

    suspend fun updatePlanMemo(memo: PlanMemo) {
        localSource.updatePlanMemo(memo)
    }

    suspend fun insertPlanMemo(memo: PlanMemo) {
        localSource.insertPlanMemo(memo)
    }


    /**
     * 전달 받은 date값과 대응되는 info data가 있는지를 확인한다.
     *
     * 일반적으론 사용자가 플랜을 만들었을 때 PlanInfo가 생성되지만, 새로운 날짜가 되었을 때는 그에 대응되는 info데이터가 없으므로
     * info데이터가 있는지를 확인한다.
     *
     * true면 데이터가 있는 것, false면 데이터가 없는 것이다.
     * */
    private suspend fun checkWhetherExistInfoDataByDate(date: LocalDate): Boolean =
        localSource.getAllPlanInfoByDate(date).isNotEmpty()


    /**
     * item data가 있는지를 확인한다.
     *
     * item은 사용자가 등록한 플랜에 대한 메타데이터이며( 플랜 이름 등), 날짜와 무관하게 관리된다.
     * 그러므로 이 데이터가 존재하지 않는다는 것은 사용자가 처음 이 앱을 사용하는 상황일 것이다... (만약 다 삭제한게 아니라)
     *
     * true면 데이터가 있는 것, false면 데이터가 없는 것이다.
     * */
    private suspend fun getAllPlanItem(): List<PlannerItemEntity> =
        localSource.getAllPlanItem()


    private fun generateInfoData(id : String, date : LocalDate) =
        PlannerInfoEntity(0, date, 0, id)
}
