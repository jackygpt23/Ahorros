package com.example.ahorros

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ahorros.data.AppDatabase
import kotlinx.coroutines.launch

class ResumenActivity : AppCompatActivity() {

    private lateinit var tvSaldo: TextView
    private lateinit var tvGastos: TextView
    private lateinit var tvAhorros: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        tvSaldo = findViewById(R.id.tvSaldo)
        tvGastos = findViewById(R.id.tvGastos)
        tvAhorros = findViewById(R.id.tvAhorros)

        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val transacciones = db.transaccionDao().getTransacciones()
            val compras = db.compraDao().getCompras()

            val ingresos = transacciones.filter { it.tipo == "ingreso" }.sumOf { it.monto }
            val egresos = transacciones.filter { it.tipo == "egreso" }.sumOf { it.monto }
            val saldo = ingresos - egresos

            val totalGastos = compras.sumOf { it.total }

            val detalles = compras.flatMap { db.compraDao().getDetalles(it.id) }
            val totalAhorros = detalles.sumOf { it.descuento * it.cantidad }

            tvSaldo.text = "Saldo Actual: $saldo"
            tvGastos.text = "Gastos en Compras: $totalGastos"
            tvAhorros.text = "Ahorros Totales: $totalAhorros"
        }
    }

    private fun Unit.transaccionDao() {
        TODO("Not yet implemented")
    }
}
