package com.example.moviles_comp_2021_p1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val botonIrACicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        val botonIrListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonIrACicloVida.setOnClickListener { abrirActividad(ACicloVida::class.java) }
        botonIrListView.setOnClickListener { abrirActividad(BListView::class.java) }
    }

    fun abrirActividad(clase: Class<*>){
                                //clase
        val intentExplicito = Intent(
            this,
            clase
        )
        //puedo poner this opcional this.startActivity
        startActivity(intentExplicito)
    }
}