package com.anugrahdev.app.klikPaket.ui.shipmentcost

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.app.klikPaket.data.models.cities.CitiesResult
import com.anugrahdev.app.klikPaket.data.models.districts.DistrictsResult
import com.anugrahdev.app.klikPaket.data.models.shippingcost.ShippingCostResult
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.data.repositories.CostRepository
import kotlinx.coroutines.launch
import java.io.IOException

class CostViewModel @ViewModelInject constructor(val repository: CostRepository) : ViewModel() {

    private val _cities = MutableLiveData<Resource<List<CitiesResult>>>()
    val cities: LiveData<Resource<List<CitiesResult>>> get() = _cities

    private val _districts = MutableLiveData<Resource<List<DistrictsResult>>>()
    val districts: LiveData<Resource<List<DistrictsResult>>> get() = _districts

    private val _shippingCost = MutableLiveData<Resource<List<ShippingCostResult>>>()
    val shippingCost: LiveData<Resource<List<ShippingCostResult>>>
        get() = _shippingCost

    fun getCities(query: String) = viewModelScope.launch {
        _cities.postValue(Resource.Loading())
        try {
            val response = repository.getCities(query)
            _cities.postValue(Resource.Success(response.data.results))
        } catch (e: IOException) {
            _cities.postValue(Resource.Error(e.message))
        }
    }

    fun getDistricts(query: String) = viewModelScope.launch {
        _districts.postValue(Resource.Loading())
        try {
            val response = repository.getDistricts(query)
            _districts.postValue(Resource.Success(response.data.results))
        } catch (e: IOException) {
            _districts.postValue(Resource.Error(e.message))
        }
    }

    fun postShippingCost(originId: Int, destId: Int, weight: Int, courier: String) =
        viewModelScope.launch {
            _shippingCost.postValue(Resource.Loading())
            try {
                val response = repository.postShippingCosts(originId, destId, weight, courier)
                _shippingCost.postValue(Resource.Success(response.data.results))
            } catch (e: IOException) {
                _shippingCost.postValue(Resource.Error(e.message))
            }
        }
}




