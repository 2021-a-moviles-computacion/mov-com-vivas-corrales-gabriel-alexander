package com.example.examen02

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Pelis_View : AppCompatActivity() {
    var posicionItemSeleccionado = 0
    var tituloPeliSeleccionada = ""
    var idPeliSeleccionada = ""
    var nombreDirector = ""
    var idDirector = ""
    var latitud = 0.0
    var longitud =0.0
    lateinit var peliculas :ArrayList<Pelicula>
    lateinit var adaptador: ArrayAdapter<Pelicula>
    lateinit var listPeliculasView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelis_view)
        idDirector = intent.getStringExtra("idDirector").toString()
        listPeliculasView = findViewById(R.id.listview_Peliculas)
        val botonVerUbicacion = findViewById<Button>(R.id.btn_ver_ubicacion)
        //--------------------------------------------------------------------
        val arregloPeliculas = mutableListOf<Pelicula>()
        val db = Firebase.firestore
        val referencia = db.collection("pelicula")
        referencia.whereEqualTo("director", idDirector).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val pelicula = document.toObject(Pelicula::class.java)
                pelicula.id = document.id
                arregloPeliculas.add(pelicula)
                adaptador.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Log.i("firebase-firestore", "Error leyendo coleccion")
        }
        peliculas= arregloPeliculas as ArrayList<Pelicula>
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1,peliculas)
        //---------------------------------------------------------------------
        listPeliculasView.adapter = adaptador
        val txtDirector = findViewById<TextView>(R.id.txt_NombreDirectorPeli)
        nombreDirector = intent.getStringExtra("nombreDirector").toString()

        txtDirector.text = nombreDirector
        val botonCrearPelicula = findViewById<Button>(R.id.btn_crear_Pelicula)
        botonCrearPelicula.setOnClickListener {
            abrirActividadConParametros(nombreDirector, idDirector, "","", AnadirPelicula::class.java)
        }
        registerForContextMenu(listPeliculasView)
        val infoPelicula = findViewById<TextView>(R.id.txt_infoMovies)
        listPeliculasView.setOnItemClickListener { _, _, position, _ ->
            val peliSelect = listPeliculasView.getItemAtPosition(position) as Pelicula
            latitud= peliSelect.latitud!!
            longitud=peliSelect.longitud!!
            botonVerUbicacion.visibility = VISIBLE
            infoPelicula.text = peliSelect.imprimirDatosPelicula()
            botonVerUbicacion.setOnClickListener {
                Log.i("firebase", "$latitud ; $longitud ")
                abrirUbicacion(latitud,longitud,peliSelect.titulo!!, Ubicacion::class.java)
            }
            return@setOnItemClickListener
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo //AS cast
        posicionItemSeleccionado = info.position

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        adaptador.notifyDataSetChanged()
        val db = Firebase.firestore
        val referencia = db.collection("pelicula")
        val peliculaSeleccionada = listPeliculasView.getItemAtPosition(posicionItemSeleccionado) as Pelicula
        tituloPeliSeleccionada = peliculaSeleccionada.titulo!!
        idPeliSeleccionada = peliculaSeleccionada.id!!
        listPeliculasView.adapter = adaptador
        val cancelarClick = { _: DialogInterface, _: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }
        val eliminarClick = { _: DialogInterface, _: Int ->
            Log.i("firebase", "Titulo Movie: $tituloPeliSeleccionada")
            referencia.document(idPeliSeleccionada)
                .delete()
                .addOnSuccessListener { Log.i("firebase", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.i("firebase", "Error deleting document", e) }
            peliculas.remove(peliculaSeleccionada)
            adaptador.notifyDataSetChanged()
            Toast.makeText(this, "Eliminada", Toast.LENGTH_SHORT).show()
        }
        return when (item.itemId) {
            R.id.menu_editar -> {
                abrirActividadConParametros(nombreDirector, idDirector, tituloPeliSeleccionada,idPeliSeleccionada, AnadirPelicula::class.java)
                return true
            }
            R.id.menu_eliminar -> {
                val advertencia = AlertDialog.Builder(this)
                advertencia.setTitle("Eliminar")
                advertencia.setMessage("Seguro de eliminar?")
                advertencia.setNegativeButton(
                    "Cancelar",
                    DialogInterface.OnClickListener(function = cancelarClick)
                )
                advertencia.setPositiveButton(
                    "Eliminar", DialogInterface.OnClickListener(
                        function = eliminarClick
                    )
                )
                advertencia.show()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirUbicacion(latitud:Double, longitud: Double,titulo:String, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("titulo", titulo)
        intentExplicito.putExtra("latitud", latitud)
        intentExplicito.putExtra("longitud", longitud)
        startActivity(intentExplicito)
    }


    private fun abrirActividadConParametros(director: String, idiDirector: String, titulo: String,idPelicula:String, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        intentExplicito.putExtra("idDirector", idiDirector)
        intentExplicito.putExtra("idPelicula", idPelicula)
        intentExplicito.putExtra("tituloPelicula", titulo)
        startActivity(intentExplicito)
    }
}