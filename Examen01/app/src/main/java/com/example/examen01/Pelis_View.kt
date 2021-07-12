package com.example.examen01

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class Pelis_View : AppCompatActivity() {
    var posicionItemSeleccionado = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelis_view)
        val nombreDirector = intent.getStringExtra("nombreDirector")
        val txtDirector = findViewById<TextView>(R.id.txt_NombreDirectorPeli)
        txtDirector.text = nombreDirector
        val peliculas = BaseDatosDirectorOR.arrayDirectores
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, peliculas)
        val listPeliculasView = findViewById<ListView>(R.id.listview_Peliculas)
        listPeliculasView.adapter = adaptador
        val botonCrearPelicula = findViewById<Button>(R.id.btn_crear_Pelicula)
        botonCrearPelicula.setOnClickListener {
            if (nombreDirector != null) abrirActividadConParametros(nombreDirector,AnadirPelicula::class.java)
        }
        registerForContextMenu(listPeliculasView)
        val infoPelicula = findViewById<TextView>(R.id.txt_infoMovies)
        listPeliculasView.setOnItemClickListener { parent, view, position, id ->
            val peliSelect = listPeliculasView.getItemAtPosition(position) as Director
            infoPelicula.setText("Nombre: ${peliSelect.nacimiento} \n")
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

    fun eliminarDirectorAlListView(
        indice: Int,
        arreglo: ArrayList<Director>,
        adaptador: ArrayAdapter<Director>
    ) {
        arreglo.removeAt(indice)
        adaptador.notifyDataSetChanged() //Actualizamos interfaz
    }

        override fun onContextItemSelected(item: MenuItem): Boolean {
            val directores = BaseDatosDirectorOR.arrayDirectores
            val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
            val listDirectoresView = findViewById<ListView>(R.id.listview_Peliculas)
            listDirectoresView.adapter = adaptador
            val cancelarClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
            }
            val eliminarClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                eliminarDirectorAlListView(posicionItemSeleccionado, directores, adaptador)
            }
            return when (item.itemId) {
                R.id.menu_editar -> {
                    abrirActividad(AnadirDirector::class.java)
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

    fun abrirActividadConParametros(director: String, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        startActivity(intentExplicito)
    }
        fun abrirActividad(clase: Class<*>) {
            val intentExplicito = Intent(this, clase)
            startActivity(intentExplicito)
        }



}