package com.example.examen02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AnadirPelicula : AppCompatActivity() {
    var latitud = 0.0
    var longitud =0.0
    var ciudadSeleccionado = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_pelicula)
        val nombreDirector = intent.getStringExtra("nombreDirector")
        val idDirector = intent.getStringExtra("idDirector")
        val idPelicula = intent.getStringExtra("idPelicula")
        //**************************************************************************************************
        val ciudadades = arrayOf("Amsterdam", "Berlin", "Bogotá", "London", "Madrid", "México DC", "New York",
            "Paris", "Quito", "Rio de Janeiro", "Tokyo", "Vienna", "Zürich")
        val latitudes = arrayOf(52.35573356081986, 52.5192880232342, 4.708507195958197, 51.509162838374365, 40.41800636913152, 19.432671298400965,
            40.73378562762449, 48.85901815928653, -0.21922483476220822,-22.938658330236088,35.68614253218143,48.217399805696736,47.36894099516527)
        val longitudes = arrayOf(4.881766688070693, 13.409614318972272, -74.05425716885155, -0.12767985390655445, -3.7077094446139935, -99.12723505493796,
            -73.99320893340935,2.296522110492033, -78.51152304364815, -43.228146943991, 139.78434424093086, 16.399427792363813, 8.550536094895179)
        val adapterCiudad: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ciudadades)
        val spinnerCiudades = findViewById<Spinner>(R.id.spinner_ubicacion)
        adapterCiudad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCiudades.adapter = adapterCiudad
        spinnerCiudades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                ciudadSeleccionado = parent?.getItemAtPosition(position) as String
                latitud=latitudes[position]
                longitud=longitudes[position]
                Log.i("ciudades", "$position")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.i("ciudades", "Item no seleccionado")
            }
        }
        //***************************************************************************************************


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
                Log.i("firestore", "$which $isChecked")
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
            //Cambio Título de la Actividad de Registrar a Actualizar
            val tituloActualizarDirector = findViewById<TextView>(R.id.txt_registroPelicula)
            tituloActualizarDirector.text = getString(R.string.actualizar_Película_Titulo)
            //Obtengo Datos actuales de la Película y los paso a TextFields
            val db = Firebase.firestore
            val referencia = db.collection("pelicula").document(idPelicula!!)
            referencia.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val peliculaParaEditar = document.toObject(Pelicula::class.java)
                        txttituloPelicula.text = peliculaParaEditar?.titulo
                        txtAnio.text = peliculaParaEditar?.anioEstreno.toString()
                        txtDuracion.text = peliculaParaEditar?.duracion.toString()
                        valoracion.rating = peliculaParaEditar?.valoracion!!
                        txtGenero.text = peliculaParaEditar?.genero
                    } else {
                        Log.d("firebase", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("firebase", "get failed with ", exception)
                }

            guardar.setOnClickListener {
                val tituloActualizado = txttituloPelicula.text.toString()
                val anioActualizada = txtAnio.text.toString()
                val duracionActualizada = txtDuracion.text.toString()
                val valoracionActualizada = valoracion.rating
                //Reviso si los TextFields están llenos
                if (!revisarText(tituloActualizado, anioActualizada, duracionActualizada, txtGenero)) {
                    val peliculaActualizada = hashMapOf<String, Any>(
                        "titulo" to tituloActualizado,
                        "anioEstreno" to anioActualizada.toInt(),
                        "duracion" to duracionActualizada.toInt(),
                        "valoracion" to valoracionActualizada,
                        "genero" to txtGenero.text.toString(),
                        "latitud" to latitud,
                        "longitud" to longitud,
                        "ubicacion" to ciudadSeleccionado
                    )
                    referencia.update(peliculaActualizada)
                        .addOnSuccessListener { Log.i("firebase", "Transaccion completa") }
                        .addOnFailureListener { Log.i("firebase", "ERROR al actualizar") }


                    Toast.makeText(this, "Película Actualizada", Toast.LENGTH_SHORT).show()
                    if (nombreDirector != null) {
                        abrirActividadConParametros(nombreDirector, idDirector!!, Pelis_View::class.java)
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
                    val db = Firebase.firestore
                    //no puedo poner ID
                    val nuevaPelicula = hashMapOf<String, Any>(
                        "director" to idDirector!!,
                        "titulo" to tituloAGuardar,
                        "anioEstreno" to anioAGuardar.toInt(),
                        "duracion" to duracionAGuardar.toInt(),
                        "valoracion" to valoracionNum,
                        "genero" to txtGenero.text.toString(),
                        "latitud" to latitud,
                        "longitud" to longitud,
                        "ubicacion" to ciudadSeleccionado
                    )
                    db.collection("pelicula").add(nuevaPelicula)
                        .addOnSuccessListener {
                            Log.i("firebase-firestore", "Pelicula añadida")
                        }.addOnFailureListener {
                            Log.i("firebase-firestore", "Error al añadirPelicula")
                        }

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

    private fun abrirActividadConParametros(director: String, idiDirector: String, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        intentExplicito.putExtra("idDirector", idiDirector)
        startActivity(intentExplicito)
    }
}