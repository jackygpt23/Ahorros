package com.example.ahorros.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaccion")
data class Transaccion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val nombre: String,
    val tipo: String, // ingreso o egreso
    val monto: Double
)
