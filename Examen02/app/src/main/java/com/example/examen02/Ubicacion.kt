package com.example.examen02

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Ubicacion : AppCompatActivity() {
    private lateinit var mapa: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion)
        val tituloPelicula = intent.getStringExtra("titulo").toString()
        val txtTituloPeli = findViewById<TextView>(R.id.txt_titulo_pelicula)
        txtTituloPeli.text=tituloPelicula
        val latitud = intent.getDoubleExtra("latitud",0.0)
        val longitud = intent.getDoubleExtra("longitud",0.0)
        solicitarPermisos()
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync {
            mapa=it
            establecerConfiguracionMapa()
            //MARCADOR DE UBICACION PELI
            val ubicacion = LatLng(latitud, longitud)
            val zoom = 12f
            anadirMarcador(ubicacion, tituloPelicula)
            moverCamaraConZoom(ubicacion, zoom)
        }
    }

    fun anadirMarcador(latLng: LatLng, title: String) {
        mapa.addMarker(MarkerOptions().position(latLng).title(title))
    }

    fun moverCamaraConZoom(latLng: LatLng, zoom: Float) {
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun establecerConfiguracionMapa() {
        val contexto = this.applicationContext
        with(mapa) {
            val permisosFineLocation = ContextCompat.checkSelfPermission(contexto, android.Manifest.permission.ACCESS_FINE_LOCATION)
            val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
            if (tienePermisos) {
                mapa.isMyLocationEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
    }


    fun solicitarPermisos() {
        val contexto = this.applicationContext
        val permisosFineLocation = ContextCompat.checkSelfPermission(contexto, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
        if (!tienePermisos) {
            ActivityCompat.requestPermissions(
                this, arrayOf(//Arreglo Permisos
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), 1 //cODIGO DE PETICION DE LOS PERMISOS
            )
        }
    }
}