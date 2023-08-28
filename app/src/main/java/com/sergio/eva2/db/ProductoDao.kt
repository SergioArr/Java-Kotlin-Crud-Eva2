package com.sergio.eva2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductoDao {
    @Query("SELECT * FROM producto ORDER BY adquirido")
    fun findAll():List<Producto>

    @Query("SELECT COUNT (*) FROM producto")
    fun contar():Int

    @Insert
    fun insertProduct(product:Producto):Long

    @Update
    fun updateProduct(product:Producto)

    @Delete
    fun deleteProduct(product:Producto)
}