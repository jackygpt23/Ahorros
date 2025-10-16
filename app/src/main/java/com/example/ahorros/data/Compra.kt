package com.example.ahorros.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compra")
data class Compra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val latitud: Double,
    val longitud: Double,
    val local: String,
    val total: Double
)
