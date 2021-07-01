package com.example.moviles_comp_2021_p1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class CIntentExplicitoParametros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cintent_explicito_parametros)

        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val edad = intent.getIntExtra("edad", 0)
        val entrenador = intent.getParcelableExtra<BEntrenador>("entrenador")

        Log.i("intent_explicito", "$nombre")
        Log.i("intent_explicito", "$apellido")
        Log.i("intent_explicito", "$edad")
        Log.i("intent_explicito", "$entrenador")

        val botonDevolverRespuesta = findViewById<Button>(R.id.btn_devolver_respuesta)
        botonDevolverRespuesta.setOnClickListener { val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("nombremodificado", "Carlos")
            intentDevolverParametros.putExtra("apellidomodificado", "CAJAS")
            intentDevolverParametros.putExtra("edadmodificado", 50)
            intentDevolverParametros.putExtra("entrenadormodificado", BEntrenador("Pedro", "Perez"))
            setResult(RESULT_OK,intentDevolverParametros)
            finish() //this.finish()
        }


    }
}