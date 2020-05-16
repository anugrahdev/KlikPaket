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

    @Query("SELECT * FROM WaybillData WHERE type==:type")
    fun getSavedWaybill(type:String): LiveData<List<WaybillData>>

}