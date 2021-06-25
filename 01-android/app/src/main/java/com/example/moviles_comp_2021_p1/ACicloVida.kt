package com.example.moviles_comp_2021_p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class ACicloVida : AppCompatActivity() {
    var numero = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aciclo_vida)
        Log.i("ciclo-vida","OnCreate")
        val textViewACicloVida=findViewById<TextView>(
            R.id.txv_ciclo_vida
        )
        textViewACicloVida.text=numero.toString()
        val buttonACicloVida=findViewById<Button>(
            R.id.btn_aumentar_ciclo_vida
        )
        buttonACicloVida.setOnClickListener { aumentarNumero() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            //Guardamos solo primitivos
            putInt("numeroGuardado", numero)
        }
        super.onSaveInstanceState(outState)
        Log.i("ciclo-vida","onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val numeroRecuperado = savedInstanceState.getInt( "numeroGuardado")
        if (numeroRecuperado !=null){
            Log.i("ciclo-vida", "numero recuperado ${numeroRecuperado}")
            numero=numeroRecuperado
            val textViewACicloVida=findViewById<TextView>(
                R.id.txv_ciclo_vida
            )
            textViewACicloVida.text=numero.toString()
        }
        Log.i("ciclo-vida","onRestoreInstanceState")
    }



    fun aumentarNumero(){
        numero += 1
        val textViewACicloVida=findViewById<TextView>(
            R.id.txv_ciclo_vida
        )
        textViewACicloVida.text=numero.toString()

    }

    override fun onStart() {
        super.onStart()
        Log.i("ciclo-vida","OnStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("ciclo-vida","OnRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("ciclo-vida","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("ciclo-vida","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("ciclo-vida","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ciclo-vida","onDestroy")
    }
}