package com.anugrahdev.app.klikPaket.ui.shipmentcost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahdev.app.klikPaket.data.repositories.CostRepository

class CostViewModelFactory(private val repository: CostRepository):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CostViewModel(repository) as T
    }

}