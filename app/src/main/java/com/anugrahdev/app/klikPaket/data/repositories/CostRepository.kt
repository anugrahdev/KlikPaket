package com.anugrahdev.app.klikPaket.data.repositories

import com.anugrahdev.app.klikPaket.data.models.cities.CitiesResult
import com.anugrahdev.app.klikPaket.data.models.districts.DistrictsResult
import com.anugrahdev.app.klikPaket.data.models.shippingcost.ShippingCostResult
import com.anugrahdev.app.klikPaket.data.network.ApiService
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.data.network.SafeApiRequest

class CostRepository(private val api:ApiService):
    SafeApiRequest() {

    suspend fun getCities(query: String):Resource<List<CitiesResult>>{
        val response= apiRequest { api.getCities(query) }
        return Resource.Success(response.data.results)
    }

    suspend fun getDistricts(query: String):Resource<List<DistrictsResult>>{
        val response = apiRequest { api.getDistricts(query) }
        return Resource.Success(response.data.results)
    }

    suspend fun postShippingCosts(originId:Int, destId:Int, weight:Int, courier:String):Resource<List<ShippingCostResult>>{
        val response = apiRequest { api.postShippingCost(originId,destId,weight,courier) }
        return Resource.Success(response.data.results)
    }

}