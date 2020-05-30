package com.anugrahdev.app.klikPaket.data.repositories

import com.anugrahdev.app.klikPaket.data.models.cities.CitiesResult
import com.anugrahdev.app.klikPaket.data.models.districts.DistrictsResult
import com.anugrahdev.app.klikPaket.data.models.shippingcost.ShippingCostResult
import com.anugrahdev.app.klikPaket.data.network.ApiService
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.data.network.SafeApiRequest

class CostRepository(private val api:ApiService):
    SafeApiRequest() {

    suspend fun getCities(query: String) =  apiRequest { api.getCities(query) }

    suspend fun getDistricts(query: String) =  apiRequest { api.getDistricts(query) }

    suspend fun postShippingCosts(originId:Int, destId:Int, weight:Int, courier:String)
            = apiRequest { api.postShippingCost(originId,destId,weight,courier) }

}