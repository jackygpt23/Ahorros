package com.example.ahorros

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.ahorros.data.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegistrarCompraActivity : AppCompatActivity() {

    private lateinit var etLocal: EditText
    private lateinit var etProducto: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etCantidad: EditText
    private lateinit var etDescuento: EditText
    private lateinit var btnAgregarProducto: Button
    private lateinit var btnGuardarCompra: Button
    private lateinit var lvProductos: ListView
    private val listaProductos = mutableListOf<DetalleCompraTemp>()
    private lateinit var adapter: ArrayAdapter<String>

    // Variables de geolocalización
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_compra)

        // Vincular elementos del layout
        etLocal = findViewById(R.id.etLocal)
        etProducto = findViewById(R.id.etProducto)
        etPrecio = findViewById(R.id.etPrecio)
        etCantidad = findViewById(R.id.etCantidad)
        etDescuento = findViewById(R.id.etDescuento)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)
        btnGuardarCompra = findViewById(R.id.btnGuardarCompra)
        lvProductos = findViewById(R.id.lvProductos)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lvProductos.adapter = adapter

        // Inicializar FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        obtenerUbicacion() // Pedir ubicación al inicio

        // Agregar producto temporalmente a la lista
        btnAgregarProducto.setOnClickListener {
            val nombre = etProducto.text.toString()
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val cantidad = etCantidad.text.toString().toIntOrNull() ?: 0
            val descuento = etDescuento.text.toString().toDoubleOrNull() ?: 0.0
            val total = cantidad * (precio - descuento)

            if (nombre.isEmpty() || cantidad <= 0) {
                Toast.makeText(this, "Ingrese nombre y cantidad válidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val detalle = DetalleCompraTemp(nombre, precio, cantidad, descuento, total)
            listaProductos.add(detalle)
            adapter.add("${detalle.nombreProducto} x${detalle.cantidad} - Total: ${detalle.totalProducto}")
            adapter.notifyDataSetChanged()

            etProducto.text.clear()
            etPrecio.text.clear()
            etCantidad.text.clear()
            etDescuento.text.clear()
        }

        // Guardar compra y detalles en Room
        btnGuardarCompra.setOnClickListener {
            val local = etLocal.text.toString()
            if (local.isEmpty() || listaProductos.isEmpty()) {
                Toast.makeText(this, "Ingrese el local y agregue productos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val totalCompra = listaProductos.sumOf { it.totalProducto }

            val compra = Compra(
                fecha = fecha,
                latitud = latitud,
                longitud = longitud,
                local = local,
                total = totalCompra
            )

            val db = AppDatabase.getDatabase(this)

            lifecycleScope.launch {
                val idCompra = db.compraDao().insertarCompra(compra).toInt()

                listaProductos.forEach {
                    val detalle = DetalleCompra(
                        idCompra = idCompra,
                        nombreProducto = it.nombreProducto,
                        precio = it.precio,
                        cantidad = it.cantidad,
                        descuento = it.descuento,
                        totalProducto = it.totalProducto
                    )
                    db.compraDao().insertarDetalle(detalle)
                }

                runOnUiThread {
                    Toast.makeText(this@RegistrarCompraActivity, "Compra guardada con ubicación", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    // Función para obtener ubicación actual
    private fun obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitud = location.latitude
                longitud = location.longitude
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacion()
        }
    }
}

// Clase temporal para manejar productos antes de guardar
data class DetalleCompraTemp(
    val nombreProducto: String,
    val precio: Double,
    val cantidad: Int,
    val descuento: Double,
    val totalProducto: Double
)
