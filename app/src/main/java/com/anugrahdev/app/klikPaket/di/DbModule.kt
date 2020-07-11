package com.anugrahdev.app.klikPaket.di

import android.content.Context
import androidx.room.Room
import com.anugrahdev.app.klikPaket.data.db.AppDatabase
import com.anugrahdev.app.klikPaket.utils.Constant.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideWaybillDataDao(
        db: AppDatabase
    )= db.getWaybillDao()

}