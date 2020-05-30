package com.anugrahdev.app.klikPaket.ui.trackwaybill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anugrahdev.app.klikPaket.data.models.waybill.WaybillData
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.data.repositories.WaybillRepository
import com.anugrahdev.app.klikPaket.utils.ApiException
import com.anugrahdev.app.klikPaket.utils.NoConnectivityException
import kotlinx.coroutines.launch

class WaybillViewModel(private val repository: WaybillRepository) : ViewModel(){

    private val _waybillData = MutableLiveData<Resource<WaybillData>>()
    val waybillData: LiveData<Resource<WaybillData>>
        get() = _waybillData

    fun getWaybillData(waybill: String, courier: String) = viewModelScope.launch {
        _waybillData.postValue(Resource.Loading())
        try{
            val response = repository.postWaybill(waybill, courier)
            _waybillData.postValue(Resource.Success(response.data))
        }catch (e: NoConnectivityException) {
            _waybillData.postValue(Resource.Error(e.message))
        } catch (e: ApiException){
            _waybillData.postValue(Resource.Error(e.message))
        }catch (e:Exception) {
            _waybillData.postValue(Resource.Error("Not Found"))
        }
    }

    fun saveWaybill(waybillData: WaybillData) = viewModelScope.launch {
        repository.upsert(waybillData)
    }

    fun getHisotrywaybill() = repository.getHistoryWaybill()

    fun getSearchWaybill(waybillNumber:String) = repository.getSearchWaybill(waybillNumber)

    fun getSavedWaybill(status:String) = repository.getSavedWaybill(status)

    fun deleteSavedWaybill(waybillData: WaybillData) = viewModelScope.launch {
        repository.deleteSavedWaybill(waybillData)
    }

    fun updateWaybill(waybillData: WaybillData) = viewModelScope.launch {
        repository.update(waybillData)
    }

    fun updateSaveData(trackTime:String, status:String, saved:Boolean, waybillNumber: String) = viewModelScope.launch {
        repository.updateSaved(trackTime,status, saved, waybillNumber)
    }

    fun clearHistory() = viewModelScope.launch {
        repository.clearHistory()
    }

}
