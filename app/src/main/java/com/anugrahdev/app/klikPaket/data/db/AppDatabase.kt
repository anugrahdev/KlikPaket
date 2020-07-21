package com.anugrahdev.app.klikPaket.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anugrahdev.app.klikPaket.data.db.dao.WaybillDataDao
import com.anugrahdev.app.klikPaket.data.models.waybill.WaybillData


@Database(
    entities = [WaybillData::class],
    version = 1
)

abstract class AppDatabase:RoomDatabase() {
    abstract fun getWaybillDao():WaybillDataDao

}