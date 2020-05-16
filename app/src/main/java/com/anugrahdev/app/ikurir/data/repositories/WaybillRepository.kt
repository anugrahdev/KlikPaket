package com.anugrahdev.app.ikurir.data.repositories

import com.anugrahdev.app.ikurir.data.db.AppDatabase
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillData
import com.anugrahdev.app.ikurir.data.network.ApiService
import com.anugrahdev.app.ikurir.utils.SafeApiRequest

class WaybillRepository (private val api:ApiService, private val db:AppDatabase):SafeApiRequest(){

    suspend fun postWaybill(waybill:String,courier:String) = apiRequest { api.postWaybill(waybill, courier) }

    suspend fun upsert(waybillData: WaybillData) = db.getWaybillDao().upsert(waybillData)

    fun getSavedWaybill() = db.getWaybillDao().getSavedWaybill("history")

    suspend fun deleteSavedWaybill(waybillData: WaybillData) = db.getWaybillDao().delete(waybillData)




}