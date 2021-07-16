package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlin.collections.ArrayList

class AnadirPelicula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_pelicula)
        val nombreDirector = intent.getStringExtra("nombreDirector")
        val idDirector = intent.getIntExtra("idDirector", 0)
        val txtDirector = findViewById<TextView>(R.id.txt_nombreDirector)
        txtDirector.text = nombreDirector

        val txttituloPelicula = findViewById<TextView>(R.id.txt_tituloPelicula)
        val txtAnio = findViewById<TextView>(R.id.txt_ingresoAnio)
        val txtDuracion = findViewById<TextView>(R.id.txt_duracion)
        val txtGenero = findViewById<TextView>(R.id.txt_selectGenero)
        val valoracion = findViewById<RatingBar>(R.id.ratingBar)
        val guardar = findViewById<Button>(R.id.btn_guardarPelicula)
        val selectGeneros = ArrayList<Int>()
        val generosSeleccionados = BooleanArray(12) { false }
        txtGenero.setOnClickListener {
            val opciones = resources.getStringArray(R.array.generos_dialogo)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Géneros")
            builder.setCancelable(false)//NO se puede cancelar
            builder.setMultiChoiceItems(
                opciones, generosSeleccionados
            ) { _, which, isChecked ->
                if (isChecked) {
                    selectGeneros.add(which)
                    selectGeneros.sort()
                } else {
                    selectGeneros.remove(which)
                }
                Log.i("bdd", "$which $isChecked")
            }
            builder.setPositiveButton("OK") { _, _ ->
                var generos = ""
                selectGeneros.forEach { i -> generos = generos + opciones[i] + "/" }
                if (generos == "") {
                    txtGenero.text = getString(R.string.seleccione)
                } else {
                    txtGenero.text = generos
                }
            }
            builder.show()
        }

        val tituloPelicula = intent.getStringExtra("tituloPelicula") //Pa Editar

        //Si no pasamos TituloPelicula es Creacion, caso contrario es Actualizacion
        if (tituloPelicula != null && tituloPelicula != "") {
            Log.i("bdd","$tituloPelicula")
            //Cambio Título de la Actividad de Registrar a Actualizar
            val tituloActualizarDirector = findViewById<TextView>(R.id.txt_registroPelicula)
            tituloActualizarDirector.text = getString(R.string.actualizar_Película_Titulo)
            //Obtengo Datos actuales de la Película y los paso a TextFields
            val peliculaParaEditar = BaseDatos.Tablas!!.consultarPeliculaPorTitulo(tituloPelicula)
            val idActualizar = peliculaParaEditar.id
            txttituloPelicula.text = peliculaParaEditar.titulo
            txtAnio.text = peliculaParaEditar.anioEstreno.toString()
            txtDuracion.text = peliculaParaEditar.duracion.toString()
            valoracion.rating = peliculaParaEditar.valoracion
            txtGenero.text = peliculaParaEditar.genero

            guardar.setOnClickListener {
                val tituloActualizado = txttituloPelicula.text.toString()
                val anioActualizada = txtAnio.text.toString()
                val duracionActualizada = txtDuracion.text.toString()
                val valoracionActualizada = valoracion.rating
                //Reviso si los TextFields están llenos
                if (!revisarText(tituloActualizado, anioActualizada, duracionActualizada, txtGenero)) {
                    BaseDatos.Tablas!!.actualizarPelicula(
                        tituloActualizado, anioActualizada.toInt(),
                        duracionActualizada.toInt(), valoracionActualizada, txtGenero.text.toString(), idActualizar
                    )
                    Toast.makeText(this, "Película Actualizada", Toast.LENGTH_SHORT).show()
                    if (nombreDirector != null) {
                        abrirActividadConParametros(nombreDirector, idDirector, Pelis_View::class.java)
                    }
                }
            }

        } else {
            guardar.setOnClickListener {
                val tituloAGuardar = txttituloPelicula.text.toString()
                val anioAGuardar = txtAnio.text.toString()
                val duracionAGuardar = txtDuracion.text.toString()
                val valoracionNum = valoracion.rating
                if (!revisarText(tituloAGuardar, anioAGuardar, duracionAGuardar, txtGenero)) {
                    BaseDatos.Tablas!!.crearPelicula(
                        tituloAGuardar, anioAGuardar.toInt(),
                        duracionAGuardar.toInt(), valoracionNum, txtGenero.text.toString(), idDirector
                    )
                    Toast.makeText(this, "Película guardada", Toast.LENGTH_SHORT).show()
                    if (nombreDirector != null) {
                        abrirActividadConParametros(nombreDirector, idDirector, Pelis_View::class.java)
                    }
                }
            }
        }
    }

    private fun revisarText(titulo: String, anio: String, duracion: String, genero: TextView): Boolean {
        return if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(anio) || TextUtils.isEmpty(duracion) || genero.text.equals("Seleccione:")) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            true
        } else {
            if (anio.toInt() > 2021 || anio.toInt() < 1 || duracion.toInt() < 1) {
                Toast.makeText(this, "Ingrese valores válidos", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
    }

    private fun abrirActividadConParametros(director: String, idiDirector: Int, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        intentExplicito.putExtra("idDirector", idiDirector)
        startActivity(intentExplicito)
    }

}