package com.anugrahdev.app.ikurir.ui.trackwaybill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahdev.app.ikurir.data.repositories.WaybillRepository
import com.anugrahdev.app.ikurir.ui.shipmentcost.CostViewModel

class TrackWaybillViewModelFactory(private val repository: WaybillRepository) :
    ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TrackWaybillViewModel(repository) as T
        }
}