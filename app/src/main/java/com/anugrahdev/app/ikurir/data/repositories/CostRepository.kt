package com.anugrahdev.app.ikurir.data.repositories

import com.anugrahdev.app.ikurir.data.network.ApiService
import com.anugrahdev.app.ikurir.utils.SafeApiRequest

class CostRepository(private val api:ApiService):SafeApiRequest() {

    suspend fun getCities(query:String) = apiRequest { api.getCities(query) }
    suspend fun getDistricts(query: String) = apiRequest { api.getDistricts(query) }
    suspend fun postShippingCost(originId:Int, destId:Int, weight:Int, courier:String)
            = apiRequest { api.postShippingCost(originId,destId,weight,courier) }

}