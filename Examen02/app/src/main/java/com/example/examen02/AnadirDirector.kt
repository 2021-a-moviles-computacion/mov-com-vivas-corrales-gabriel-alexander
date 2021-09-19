package com.example.examen02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AnadirDirector : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_director)
        val nombre = findViewById<TextView>(R.id.txt_ingresoNombre)
        val nacionalidad = findViewById<TextView>(R.id.txt_ingresoNacionalidad)
        val numPelis = findViewById<TextView>(R.id.txt_ingresoNumPelis)
        val fecha = findViewById<TextView>(R.id.txt_ingresoDate)
        val oscar = findViewById<CheckBox>(R.id.check_oscar)
        val guardar = findViewById<Button>(R.id.btn_guardarDir)
        val datePickera = findViewById<DatePicker>(R.id.datePicker1)
        val checkFecha = findViewById<CheckBox>(R.id.checkFecha)
        val calendar = Calendar.getInstance()
        checkFecha.visibility = View.INVISIBLE
        datePickera.visibility = View.INVISIBLE
        fecha.setOnClickListener {
            checkFecha.isChecked = false
            fecha.visibility = View.INVISIBLE
            datePickera.maxDate = calendar.timeInMillis
            checkFecha.visibility = View.VISIBLE
            datePickera.visibility = View.VISIBLE
            checkFecha.setOnClickListener {
                calendar.set(datePickera.year, datePickera.month, datePickera.dayOfMonth)
                fecha.text = android.text.format.DateFormat.format("MM/dd/yyyy", calendar)
                datePickera.visibility = View.INVISIBLE
                checkFecha.visibility = View.INVISIBLE
                fecha.visibility = View.VISIBLE
            }
        }

        val nombreDirector = intent.getStringExtra("nombreDirector") //Pa Editar
        val idDirector = intent.getStringExtra("idDirector")

        //Si no pasamos NombreDirector es Creacion, caso contrario es Actualizacion
        if (nombreDirector != null) {
            //Cambio Título de la Actividad de Registrar a Actualizar
            val tituloActualizarDirector = findViewById<TextView>(R.id.txt_registroDirector_Tit)
            tituloActualizarDirector.text = getString(R.string.actualizar_Director_Titulo)
            //Obtengo Datos actuales del Director y los paso a TextFields
            val db = Firebase.firestore
            val referencia = db.collection("director").document(idDirector!!)
            referencia.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val directorParaEditar = document.toObject(Director::class.java)
                        nombre.text = directorParaEditar?.nombre
                        nacionalidad.text = directorParaEditar?.nacionalidad
                        numPelis.text = directorParaEditar?.num_pelis.toString()
                        fecha.text = directorParaEditar?.nacimiento
                        if (directorParaEditar?.oscar == 1) oscar.isChecked = true
                    } else {
                        Log.d("firebase", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("firebase", "get failed with ", exception)
                }


            guardar.setOnClickListener {
                val nombreActualizado = nombre.text.toString()
                val nacionalidadActualizada = nacionalidad.text.toString()
                val numPelisActualizada = numPelis.text.toString()
                val oscarInt = if (oscar.isChecked) 1 else 0 //Convierto boolean a Int
                //Reviso si los TextFields están llenos
                if (!revisarTextLlenos(nombreActualizado, nacionalidadActualizada, numPelisActualizada, fecha)) {
                    val directorActualizado = hashMapOf<String, Any>(
                        "nombre" to nombreActualizado,
                        "nacionalidad" to nacionalidadActualizada,
                        "nacimiento" to fecha.text.toString(),
                        "num_pelis" to numPelisActualizada.toInt(),
                        "oscar" to oscarInt
                    )
                    referencia.update(directorActualizado)
                        .addOnSuccessListener { Log.i("firebase", "Transaccion completa") }
                        .addOnFailureListener { Log.i("firebase", "ERROR al actualizar") }


                    Toast.makeText(this, "Director actualizado", Toast.LENGTH_SHORT).show()
                    abrirActividad(Director_View::class.java)
                }
            }

        } else {
            guardar.setOnClickListener {
                val nombreAGuardar = nombre.text.toString()
                val nacionalidadAGuardar = nacionalidad.text.toString()
                val numPelisAGuardar = numPelis.text.toString()
                val oscarInt = if (oscar.isChecked) 1 else 0
                if (!revisarTextLlenos(nombreAGuardar, nacionalidadAGuardar, numPelisAGuardar, fecha)) {
                    val db = Firebase.firestore
                    val nuevoDirector = hashMapOf<String, Any>(
                        "nombre" to nombreAGuardar,
                        "nacionalidad" to nacionalidadAGuardar,
                        "nacimiento" to fecha.text.toString(),
                        "num_pelis" to numPelisAGuardar.toInt(),
                        "oscar" to oscarInt
                    )
                    db.collection("director").add(nuevoDirector)
                        .addOnSuccessListener {
                            Log.i("firebase-firestore", "Director añadido")
                        }.addOnFailureListener {
                            Log.i("firebase-firestore", "Error al añadirDirector")
                        }
                    Toast.makeText(this, "Director guardado", Toast.LENGTH_SHORT).show()
                    abrirActividad(Director_View::class.java)
                }
            }
        }
    }

    private fun revisarTextLlenos(nombre: String, nacionalidad: String, numPelis: String, fecha: TextView): Boolean {
        return if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(nacionalidad) || TextUtils.isEmpty(numPelis) || fecha.text.equals("MM/DD/YYYY")) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            true
        } else {
            if (numPelis.toInt() < 1) {
                Toast.makeText(this, "Ingrese valores válidos", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
    }


    private fun abrirActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

}