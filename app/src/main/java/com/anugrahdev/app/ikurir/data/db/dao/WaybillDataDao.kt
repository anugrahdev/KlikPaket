package com.anugrahdev.app.ikurir.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillData

@Dao
interface WaybillDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(waybilldata:WaybillData):Long

    @Delete
    suspend fun delete(waybilldata: WaybillData)

    @Query("SELECT COUNT(*) FROM WaybillData WHERE waybillNumber LIKE :waybillNumber AND saved==1")
    fun getSearchWaybill(waybillNumber:String): Boolean

    @Query("SELECT * FROM WaybillData WHERE waybillNumber LIKE :waybillNumber AND saved==1")
    fun getSearchWaybillData(waybillNumber:String): WaybillData

    @Query("UPDATE WaybillData SET trackTime = :trackTime, status_status = :status, saved=:saved WHERE waybillNumber ==:waybillNumber")
    suspend fun updateSaved(trackTime:String, status: String, saved:Boolean, waybillNumber: String)

    @Query("SELECT * FROM WaybillData WHERE history == 1")
    fun getHistoryWaybill(): LiveData<List<WaybillData>>

    @Query("SELECT * FROM WaybillData WHERE saved== 1 AND status_status==:status")
    fun getSavedWaybill(status:String): LiveData<List<WaybillData>>

    @Query("SELECT * FROM WaybillData WHERE saved== 1")
    fun getAllSavedWaybill(): LiveData<List<WaybillData>>

    @Update
    suspend fun update(waybilldata: WaybillData)

    @Query("UPDATE WaybillData SET history=0")
    suspend fun clearHistory()

}