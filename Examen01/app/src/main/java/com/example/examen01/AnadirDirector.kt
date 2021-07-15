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

        val nombreDirector = intent.getStringExtra("nombreDirector") //Pa Editar
        if (nombreDirector != null) {
            val tituloActualizarDirector = findViewById<TextView>(R.id.txt_registroDirector_Tit)
            tituloActualizarDirector.text = getString(R.string.actualizar_Director_Titulo)
            val directorParaEditar =
                BaseDatos.TablaDirector!!.consultarUsuarioPorNombre(nombreDirector) as Director
            val idActualizar =directorParaEditar.id
            val nombre = findViewById<TextView>(R.id.txt_ingresoNombre)
            val nacionalidad = findViewById<TextView>(R.id.txt_ingresoNacionalidad)
            val numPelis = findViewById<TextView>(R.id.txt_ingresoNumPelis)
            val fecha = findViewById<TextView>(R.id.txt_ingresoDate)
            val oscar = findViewById<CheckBox>(R.id.check_oscar)
            nombre.text = directorParaEditar.nombre
            nacionalidad.text = directorParaEditar.nacionalidad
            numPelis.text = directorParaEditar.numMovies.toString()
            fecha.text = directorParaEditar.nacimiento
            if (directorParaEditar.oscar == 1) oscar.isChecked = true

            val guardar = findViewById<Button>(R.id.btn_guardarDir)
            guardar.setOnClickListener {
                val nombre = findViewById<TextView>(R.id.txt_ingresoNombre).text.toString()
                val nacionalidad = findViewById<TextView>(R.id.txt_ingresoNacionalidad).text.toString()
                val numPelis = findViewById<TextView>(R.id.txt_ingresoNumPelis).text.toString()
                val oscar = findViewById<CheckBox>(R.id.check_oscar)
                var oscarInt = if (oscar.isChecked) 1 else 0
                if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(nacionalidad) || TextUtils.isEmpty(numPelis) ||fecha.text.equals("MM/DD/YYYY")) {
                    Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    BaseDatos.TablaDirector!!.actualizarDirector(nombre, nacionalidad, fecha.text.toString(), numPelis.toInt(), oscarInt,idActualizar)
                    Toast.makeText(this, "Director actualizado", Toast.LENGTH_SHORT).show()
                }
            }

        }else{



            val fecha = findViewById<TextView>(R.id.txt_ingresoDate)
            val datePickera = findViewById<DatePicker>(R.id.datePicker1)
            val checkFecha = findViewById<CheckBox>(R.id.checkFecha)
            checkFecha.visibility = INVISIBLE
            datePickera.visibility = INVISIBLE
            fecha.setOnClickListener {
                checkFecha.isChecked = false
                fecha.visibility = INVISIBLE
                val calendar = Calendar.getInstance()
                datePickera.maxDate = calendar.timeInMillis
                checkFecha.visibility = VISIBLE
                datePickera.visibility = VISIBLE
                checkFecha.setOnClickListener {
                    calendar.set(datePickera.year,datePickera.month,datePickera.dayOfMonth)
                    fecha.text = android.text.format.DateFormat.format("MM/dd/yyyy",calendar )
                    datePickera.visibility = INVISIBLE
                    checkFecha.visibility = INVISIBLE
                    fecha.visibility = VISIBLE
                }
            }


            val guardar = findViewById<Button>(R.id.btn_guardarDir)
            guardar.setOnClickListener {
                val nombre = findViewById<TextView>(R.id.txt_ingresoNombre).text.toString()
                val nacionalidad = findViewById<TextView>(R.id.txt_ingresoNacionalidad).text.toString()
                val numPelis = findViewById<TextView>(R.id.txt_ingresoNumPelis).text.toString()
                val oscar = findViewById<CheckBox>(R.id.check_oscar)
                var oscarInt = if (oscar.isChecked) 1 else 0

                if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(nacionalidad) || TextUtils.isEmpty(numPelis) ||fecha.text.equals("MM/DD/YYYY")) {
                    Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    BaseDatos.TablaDirector!!.crearDirector(
                        nombre,
                        nacionalidad,
                        fecha.text.toString(),
                        numPelis.toInt(),
                        oscarInt
                    )
                    // BaseDatosDirectorOR.arrayDirectores.add(Director(10,nombre.toString(),nacionalidad.toString(),fecha.text.toString(),numPelis.toString().toInt(),oscarInt))
                    Toast.makeText(this, "Director guardado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}