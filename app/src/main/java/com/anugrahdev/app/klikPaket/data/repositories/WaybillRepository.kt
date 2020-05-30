package com.anugrahdev.app.klikPaket.data.repositories

import com.anugrahdev.app.klikPaket.data.db.AppDatabase
import com.anugrahdev.app.klikPaket.data.models.waybill.WaybillData
import com.anugrahdev.app.klikPaket.data.network.ApiService
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.data.network.SafeApiRequest

class WaybillRepository (private val api:ApiService, private val db:AppDatabase):
    SafeApiRequest(){

    suspend fun postWaybill(waybill:String,courier:String) = apiRequest { api.postWaybill(waybill, courier) }

    suspend fun upsert(waybillData: WaybillData) = db.getWaybillDao().upsert(waybillData)

    fun getHistoryWaybill() = db.getWaybillDao().getHistoryWaybill()

    fun getAllSavedWaybill() = db.getWaybillDao().getAllSavedWaybill()

    fun getSavedWaybill(status:String) = db.getWaybillDao().getSavedWaybill(status)

    suspend fun deleteSavedWaybill(waybillData: WaybillData) = db.getWaybillDao().delete(waybillData)

    suspend fun update(waybillData: WaybillData) = db.getWaybillDao().update(waybillData)

    fun getSearchWaybill(waybillNumber:String) = db.getWaybillDao().getSearchWaybill(waybillNumber)

    suspend fun updateSaved(trackTime:String, status:String, saved:Boolean, waybillNumber: String)
            = db.getWaybillDao().updateSaved(trackTime, status, saved, waybillNumber)

    suspend fun clearHistory() = db.getWaybillDao().clearHistory()


}