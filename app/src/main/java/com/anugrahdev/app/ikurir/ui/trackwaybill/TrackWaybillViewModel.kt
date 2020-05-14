package com.anugrahdev.app.ikurir.ui.trackwaybill

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillData
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillDetail
import com.anugrahdev.app.ikurir.data.repositories.WaybillRepository
import com.anugrahdev.app.ikurir.utils.ApiException
import com.anugrahdev.app.ikurir.utils.anyException
import kotlinx.coroutines.launch

class TrackWaybillViewModel(private val repository: WaybillRepository) : ViewModel(){

    private var error:Boolean = false
    private val TAG = "TrackWaybillViewModel"
    private val _waybilldetails = MutableLiveData<List<WaybillDetail>>()
    val waybilldetails: LiveData<List<WaybillDetail>>
        get() = _waybilldetails

    private val _waybilldata = MutableLiveData<WaybillData>()
    val waybilldata: LiveData<WaybillData>
        get() = _waybilldata

    fun getWaybillDetails(waybill:String, courier:String) = viewModelScope.launch {
        val response = repository.postWaybill(waybill, courier)
        _waybilldetails.value = response.data.details
    }

    fun getWaybillData(waybill: String, courier: String) = viewModelScope.launch {
        try{
            val response = repository.postWaybill(waybill, courier)
            _waybilldata.value = response.data
        }catch (e:Exception){
            _waybilldata.value = null
        }
    }

    fun errorHandling():Boolean{
        return error
    }






}
