package com.anugrahdev.app.ikurir.ui.shipmentcost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.app.ikurir.data.models.cities.CitiesResult
import com.anugrahdev.app.ikurir.data.models.districts.DistrictsResult
import com.anugrahdev.app.ikurir.data.models.shippingcost.ShippingCostResult
import com.anugrahdev.app.ikurir.data.repositories.CostRepository
import com.anugrahdev.app.ikurir.utils.ApiException
import kotlinx.coroutines.launch

class CostViewModel(private val repository: CostRepository) : ViewModel() {

    val cities = MutableLiveData<List<CitiesResult>>()
    val districts = MutableLiveData<List<DistrictsResult>>()
    private val _shippingcost = MutableLiveData<List<ShippingCostResult>>()
    val shippingcost : LiveData<List<ShippingCostResult>>
        get() = _shippingcost

    fun getCities(query:String) = viewModelScope.launch {
        val response = repository.getCities(query)
        cities.postValue(response.data.results)
    }

    fun getDistricts(query: String) = viewModelScope.launch {
        val response = repository.getDistricts(query)
        districts.postValue(response.data.results)
    }

    fun postShippingCost(originId:Int, destId:Int, weight:Int, courier:String) = viewModelScope.launch {
        try{
            val response = repository.postShippingCost(originId,destId,weight,courier)
            _shippingcost.value = response.data.results
        }catch (e:ApiException){
            Log.d("CostViewModel", e.toString())
        }
    }





}
