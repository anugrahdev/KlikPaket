package com.anugrahdev.app.klikPaket.ui.trackwaybill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahdev.app.klikPaket.data.repositories.WaybillRepository

class WaybillViewModelFactory(private val repository: WaybillRepository) :
    ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WaybillViewModel(repository) as T
        }
}