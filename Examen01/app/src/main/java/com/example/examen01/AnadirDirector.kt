package com.example.examen01

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.TextView
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class AnadirDirector : AppCompatActivity() {
    //val  dateLister = DatePickerDialog.OnDateSetListener

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
                fecha.text = "${datePickera.month} / ${datePickera.dayOfMonth}   / ${datePickera.year}"
                datePickera.visibility = INVISIBLE
                checkFecha.visibility = INVISIBLE
                fecha.visibility = VISIBLE
            }
        }

        val guardar = findViewById<Button>(R.id.btn_guardarDir)
        guardar.setOnClickListener {  }
    }
}