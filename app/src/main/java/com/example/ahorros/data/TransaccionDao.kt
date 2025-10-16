package com.example.ahorros.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// TransaccionDao.kt
@Dao
interface TransaccionDao {
    @Insert suspend fun insertarTransaccion(transaccion: Transaccion)
    @Query("SELECT * FROM transaccion") suspend fun getTransacciones(): List<Transaccion>
}

