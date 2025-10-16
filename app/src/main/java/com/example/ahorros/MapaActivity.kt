package com.example.ahorros

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ahorros.data.AppDatabase
import com.example.ahorros.data.Compra
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Obtener el SupportMapFragment desde XML
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 🔹 3 marcadores literarios fijos
        val feriaLibros = LatLng(-33.447487, -70.673676)
        val feriaEscritores = LatLng(-33.45694, -70.64827)
        val eventoLiterario = LatLng(-33.4580, -70.6600)

        mMap.addMarker(
            MarkerOptions()
                .position(feriaLibros)
                .title("Feria de Libros")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )

        mMap.addMarker(
            MarkerOptions()
                .position(feriaEscritores)
                .title("Feria de Escritores")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        )

        mMap.addMarker(
            MarkerOptions()
                .position(eventoLiterario)
                .title("Evento Literario")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )

        // Centrar cámara en el primer marcador
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(feriaLibros, 14f))

        // 🔹 Agregar marcadores de compras guardadas en Room
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val compras: List<Compra> = db.compraDao().getCompras() // función suspend
            compras.forEach { compra ->
                val position = LatLng(compra.latitud, compra.longitud)
                mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title("Compra en ${compra.local}")
                        .snippet("Total: ${compra.total}")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
            }
        }
    }
}


