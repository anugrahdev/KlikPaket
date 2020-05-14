package com.anugrahdev.app.ikurir.ui.shipmentcost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anugrahdev.app.ikurir.data.repositories.CostRepository

class CostViewModelFactory(private val repository: CostRepository):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CostViewModel(repository) as T
    }

}