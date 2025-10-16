package com.example.ahorros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Crear SupportMapFragment dinámicamente
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 3 marcadores literarios
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

        // Centrar la cámara en el primer marcador
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(feriaLibros, 14f))
    }
}
