package com.anugrahdev.app.ikurir.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anugrahdev.app.ikurir.data.db.dao.WaybillDataDao
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillData


@Database(
    entities = [WaybillData::class],
    version = 1
)

abstract class AppDatabase:RoomDatabase() {
    abstract fun getWaybillDao():WaybillDataDao

    companion object{
        @Volatile
        private var instance:AppDatabase?=null
        private val LOCK=Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context)
        }

        private  fun createDatabase (context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "ikurir_db.db"
            ).build()
    }
}