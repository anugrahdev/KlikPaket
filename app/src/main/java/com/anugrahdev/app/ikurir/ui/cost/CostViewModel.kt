package com.anugrahdev.app.ikurir.ui.cost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.app.ikurir.data.models.cities.CitiesResult
import com.anugrahdev.app.ikurir.data.models.districts.DistrictsResult
import com.anugrahdev.app.ikurir.data.models.shippingcost.ShippingCostResult
import com.anugrahdev.app.ikurir.data.repositories.CostRepository
import kotlinx.coroutines.launch

class CostViewModel(private val repository: CostRepository) : ViewModel() {

    val cities = MutableLiveData<List<CitiesResult>>()
    val districts = MutableLiveData<List<DistrictsResult>>()
    val shippingcost = MutableLiveData<List<ShippingCostResult>>()

    fun getCities(query:String) = viewModelScope.launch {
        val response = repository.getCities(query)
        cities.postValue(response.data.results)
    }

    fun getDistricts(query: String) = viewModelScope.launch {
        val response = repository.getDistricts(query)
        districts.postValue(response.data.results)
    }

    fun postShippingCost(originId:Int, destId:Int, weight:Int, courier:String) = viewModelScope.launch {
        val response = repository.postShippingCost(originId,destId,weight,courier)
        shippingcost.postValue(response.data.results)
    }



}
