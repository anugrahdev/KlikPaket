package com.anugrahdev.app.klikPaket.data.repositories

import com.anugrahdev.app.klikPaket.data.db.AppDatabase
import com.anugrahdev.app.klikPaket.data.db.dao.WaybillDataDao
import com.anugrahdev.app.klikPaket.data.models.waybill.WaybillData
import com.anugrahdev.app.klikPaket.data.network.ApiService
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.data.network.SafeApiRequest
import javax.inject.Inject

class WaybillRepository @Inject constructor (val api:ApiService, val db:WaybillDataDao):
    SafeApiRequest(){

    suspend fun postWaybill(waybill:String,courier:String) = apiRequest { api.postWaybill(waybill, courier) }

    suspend fun upsert(waybillData: WaybillData) = db.upsert(waybillData)

    fun getHistoryWaybill() = db.getHistoryWaybill()

    fun getAllSavedWaybill() = db.getAllSavedWaybill()

    fun getSavedWaybill(status:String) = db.getSavedWaybill(status)

    suspend fun deleteSavedWaybill(waybillData: WaybillData) = db.delete(waybillData)

    suspend fun update(waybillData: WaybillData) = db.update(waybillData)

    fun getSearchWaybill(waybillNumber:String) = db.getSearchWaybill(waybillNumber)

    suspend fun updateSaved(trackTime:String, status:String, saved:Boolean, waybillNumber: String)
            = db.updateSaved(trackTime, status, saved, waybillNumber)

    suspend fun clearHistory() = db.clearHistory()


}