package com.anugrahdev.app.ikurir

import android.app.Application
import com.anugrahdev.app.ikurir.data.network.ApiService
import com.anugrahdev.app.ikurir.data.repositories.CostRepository
import com.anugrahdev.app.ikurir.ui.cost.CostViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.Collections.singleton

class MyApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))
        bind() from singleton { ApiService() }
        bind() from singleton { CostRepository(instance()) }
        bind() from provider { CostViewModelFactory(instance()) }


    }
}