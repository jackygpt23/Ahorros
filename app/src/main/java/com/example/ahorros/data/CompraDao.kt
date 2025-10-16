package com.example.ahorros.data

import androidx.room.*

// CompraDao.kt
@Dao
interface CompraDao {
    @Insert suspend fun insertarCompra(compra: Compra): Long
    @Insert suspend fun insertarDetalle(detalle: DetalleCompra)
    @Query("SELECT * FROM compra") suspend fun getCompras(): List<Compra>
    @Query("SELECT * FROM detalle_compra WHERE idCompra = :idCompra") suspend fun getDetalles(idCompra: Int): List<DetalleCompra>
}
