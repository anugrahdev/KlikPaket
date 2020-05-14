package com.anugrahdev.app.ikurir.data.repositories

import com.anugrahdev.app.ikurir.data.network.ApiService
import com.anugrahdev.app.ikurir.utils.SafeApiRequest

class WaybillRepository (private val api:ApiService):SafeApiRequest(){

    suspend fun postWaybill(waybill:String,courier:String) = apiRequest { api.postWaybill(waybill, courier) }

}