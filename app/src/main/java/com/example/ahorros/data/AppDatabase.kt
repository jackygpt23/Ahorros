package com.example.ahorros.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 🔹 Definir todas las entidades y versión
@Database(
    entities = [Transaccion::class, Compra::class, DetalleCompra::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val compraDao: Any

    // 🔹 Declarar todos los DAOs
    abstract fun transaccionDao(): TransaccionDao
    abstract fun compraDao(): CompraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 🔹 Devuelve la instancia única de la base de datos
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ahorros_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
