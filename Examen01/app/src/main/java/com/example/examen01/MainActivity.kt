package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val botonIniciarSistema = findViewById<Button>(R.id.btn_Iniciar_Sistema)
        botonIniciarSistema.setOnClickListener { abrirActividad(Director_View::class.java) } // "::class es uan referencia a clase

    }
    fun abrirActividad( clase: Class<*>){
        val intentExplicito = Intent(this,clase)
        startActivity(intentExplicito)
    }

}