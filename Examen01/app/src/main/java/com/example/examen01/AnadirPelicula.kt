package com.example.examen01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AnadirPelicula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_pelicula)
        val nombreDirector = intent.getStringExtra("nombreDirector")
        val txtDirector = findViewById<TextView>(R.id.txt_nombreDirector)
        txtDirector.text = nombreDirector
    }
}