package com.example.examen01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import java.util.*

class AnadirPelicula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_pelicula)
        val nombreDirector = intent.getStringExtra("nombreDirector")
        val txtDirector = findViewById<TextView>(R.id.txt_nombreDirector)
        txtDirector.text = nombreDirector

        val tituloPelicula = intent.getStringExtra("tituloPelicula") //Pa Editar

    }
}