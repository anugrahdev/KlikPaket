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

class WaybillViewModel(private val repository: WaybillRepository) : ViewModel(){

    private val TAG = "TrackWaybillViewModel"

    private val _waybilldata = MutableLiveData<WaybillData>()
    val waybilldata: LiveData<WaybillData>
        get() = _waybilldata

    fun getWaybillData(waybill: String, courier: String) = viewModelScope.launch {
        try{
            val response = repository.postWaybill(waybill, courier)
            _waybilldata.value = response.data
        }catch (e:Exception){
            _waybilldata.value = null
            Log.d(TAG, e.message.toString())
        }
    }

    fun saveWaybill(waybillData: WaybillData) = viewModelScope.launch {
        repository.upsert(waybillData)
    }

    fun getHisotrywaybill() = repository.getHistoryWaybill()

    fun getAllSavedWaybill() = repository.getAllSavedWaybill()

    fun getSavedWaybill(status:String) = repository.getSavedWaybill(status)

    fun deleteSavedWaybill(waybillData: WaybillData) = viewModelScope.launch {
        repository.deleteSavedWaybill(waybillData)
    }


}
