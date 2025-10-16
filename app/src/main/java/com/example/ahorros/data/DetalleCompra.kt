package com.example.ahorros.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "detalle_compra")
data class DetalleCompra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idCompra: Int,
    val nombreProducto: String,
    val precio: Double,
    val cantidad: Int,
    val descuento: Double,
    val totalProducto: Double
)
