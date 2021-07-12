package com.example.examen01

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import java.sql.DatabaseMetaData
import java.util.*

class AnadirDirector : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_director)
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
                fecha.text = "${datePickera.month + 1} / ${datePickera.dayOfMonth}   / ${datePickera.year}"
                datePickera.visibility = INVISIBLE
                checkFecha.visibility = INVISIBLE
                fecha.visibility = VISIBLE
            }
        }

        val nombre = findViewById<TextView>(R.id.txt_ingresoNombre).text
        val nacionalidad = findViewById<TextView>(R.id.txt_ingresoNacionalidad).text
        val numPelis = findViewById<TextView>(R.id.txt_ingresoNumPelis).text
        val oscar = findViewById<CheckBox>(R.id.check_oscar)

        val guardar = findViewById<Button>(R.id.btn_guardarDir)
        guardar.setOnClickListener {
            var oscarInt =  if(oscar.isChecked) 1 else 0
           BaseDatosDirectorOR.arrayDirectores.add(Director(10,nombre.toString(),nacionalidad.toString(),fecha.text.toString(),numPelis.toString().toInt(),oscarInt))
            Toast.makeText(this, "Director guardado", Toast.LENGTH_SHORT).show()

        }
    }
}