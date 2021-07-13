package com.example.examen01


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class AnadirDirector : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BaseDatos.TablaDirector= SQLiteHelper(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_director)
        val fecha = findViewById<TextView>(R.id.txt_ingresoDate)
        val datePickera = findViewById<DatePicker>(R.id.datePicker1)
        val checkFecha = findViewById<CheckBox>(R.id.checkFecha)
        val dateFormat = SimpleDateFormat("MM/dd/yyyy") //Formato de Fecha
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
                val fechaPreFormato: String = "${datePickera.month + 1}/${datePickera.dayOfMonth}/${datePickera.year}"
                fecha.text = dateFormat.parse(fechaPreFormato).toString()
                datePickera.visibility = INVISIBLE
                checkFecha.visibility = INVISIBLE
                fecha.visibility = VISIBLE
            }
        }


        val guardar = findViewById<Button>(R.id.btn_guardarDir)

        guardar.setOnClickListener {
            val nombre = findViewById<TextView>(R.id.txt_ingresoNombre).text.toString()
            val nacionalidad = findViewById<TextView>(R.id.txt_ingresoNacionalidad).text.toString()
            val numPelis = findViewById<TextView>(R.id.txt_ingresoNumPelis).text.toString().toInt()
            val oscar = findViewById<CheckBox>(R.id.check_oscar)
            var oscarInt =  if(oscar.isChecked) 1 else 0
            BaseDatos.TablaDirector!!.crearDirector(nombre,nacionalidad, fecha.text.toString(),numPelis,oscarInt)
          // BaseDatosDirectorOR.arrayDirectores.add(Director(10,nombre.toString(),nacionalidad.toString(),fecha.text.toString(),numPelis.toString().toInt(),oscarInt))
            Toast.makeText(this, "Director guardado", Toast.LENGTH_SHORT).show()
        }
    }
}