package com.example.examen01


import android.os.Bundle
import android.text.TextUtils
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class AnadirDirector() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BaseDatos.TablaDirector = SQLiteHelper(this)
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
        checkFecha.visibility = INVISIBLE
        datePickera.visibility = INVISIBLE
        fecha.setOnClickListener {
            checkFecha.isChecked = false
            fecha.visibility = INVISIBLE
            datePickera.maxDate = calendar.timeInMillis
            checkFecha.visibility = VISIBLE
            datePickera.visibility = VISIBLE
            checkFecha.setOnClickListener {
                calendar.set(datePickera.year, datePickera.month, datePickera.dayOfMonth)
                fecha.text = android.text.format.DateFormat.format("MM/dd/yyyy", calendar)
                datePickera.visibility = INVISIBLE
                checkFecha.visibility = INVISIBLE
                fecha.visibility = VISIBLE
            }
        }

        val nombreDirector = intent.getStringExtra("nombreDirector") //Pa Editar

        //Si no pasamos NombreDirector es Creacion, caso contrario es Actualizacion
        if (nombreDirector != null) {
            //Cambio Título de la Actividad de Registrar a Actualizar
            val tituloActualizarDirector = findViewById<TextView>(R.id.txt_registroDirector_Tit)
            tituloActualizarDirector.text = getString(R.string.actualizar_Director_Titulo)
            //Obtengo Datos actuales del Director y los paso a TextFields
            val directorParaEditar = BaseDatos.TablaDirector!!.consultarUsuarioPorNombre(nombreDirector)
            val idActualizar = directorParaEditar.id
            nombre.text = directorParaEditar.nombre
            nacionalidad.text = directorParaEditar.nacionalidad
            numPelis.text = directorParaEditar.numMovies.toString()
            fecha.text = directorParaEditar.nacimiento
            if (directorParaEditar.oscar == 1) oscar.isChecked = true

            guardar.setOnClickListener {
                val nombreActualizado = nombre.text.toString()
                val nacionalidadActualizada = nacionalidad.text.toString()
                val numPelisActualizada = numPelis.text.toString()
                var oscarInt = if (oscar.isChecked) 1 else 0 //Convierto boolean a Int
                //Reviso si los TextFields están llenos
                if (!revisarTextLlenos(nombreActualizado, nacionalidadActualizada, numPelisActualizada, fecha)) {
                    BaseDatos.TablaDirector!!.actualizarDirector(
                        nombreActualizado,
                        nacionalidadActualizada,
                        fecha.text.toString(),
                        numPelisActualizada.toInt(),
                        oscarInt,
                        idActualizar
                    )
                    Toast.makeText(this, "Director actualizado", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            guardar.setOnClickListener {
                val nombreAGuardar = nombre.text.toString()
                val nacionalidadAGuardar = nacionalidad.text.toString()
                val numPelisAGuardar = numPelis.text.toString()
                var oscarInt = if (oscar.isChecked) 1 else 0
                if (!revisarTextLlenos(nombreAGuardar,nacionalidadAGuardar,numPelisAGuardar,fecha)) {
                    BaseDatos.TablaDirector!!.crearDirector(
                        nombreAGuardar,
                        nacionalidadAGuardar,
                        fecha.text.toString(),
                        numPelisAGuardar.toInt(),
                        oscarInt
                    )
                    Toast.makeText(this, "Director guardado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun revisarTextLlenos(nombre: String, nacionalidad: String, numPelis: String, fecha: TextView): Boolean {
        return if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(nacionalidad) || TextUtils.isEmpty(numPelis) || fecha.text.equals("MM/DD/YYYY")) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            true
        } else false
    }
}