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

    @Query("SELECT * FROM WaybillData WHERE type=='history'")
    fun getHistoryWaybill(): LiveData<List<WaybillData>>

    @Query("SELECT * FROM WaybillData WHERE type=='saved' AND status_status==:status")
    fun getSavedWaybill(status:String): LiveData<List<WaybillData>>

    @Query("SELECT * FROM WaybillData WHERE type=='saved'")
    fun getAllSavedWaybill(): LiveData<List<WaybillData>>

}