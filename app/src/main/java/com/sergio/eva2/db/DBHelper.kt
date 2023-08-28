package com.sergio.eva2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Producto::class], version = 1 )
abstract class DBHelper: RoomDatabase()  {
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var BASE_DATOS: DBHelper? = null
        fun getInstance (contexto: Context): DBHelper {
            return BASE_DATOS ?: synchronized(this) {
                Room. databaseBuilder(
                    contexto.applicationContext,
                    DBHelper::class.java,
                    "productos.bd")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }

    }
}
