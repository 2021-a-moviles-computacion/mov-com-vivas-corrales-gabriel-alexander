package com.example.examen01

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class Pelis_View : AppCompatActivity() {
    var posicionItemSeleccionado = 0
    var tituloPeliSeleccionada = ""
    var nombreDirector = ""
    var idDirector = 0
    lateinit var peliculas :ArrayList<Pelicula>
    lateinit var adaptador: ArrayAdapter<Pelicula>
    lateinit var listPeliculasView: ListView

    override fun onStart() {
        super.onStart()
        BaseDatos.Tablas = SQLiteHelper(this)
        peliculas = BaseDatos.Tablas!!.consultarTodasPeliculasDeUnDirector(idDirector)
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, peliculas)
        listPeliculasView = findViewById<ListView>(R.id.listview_Peliculas)
        adaptador.notifyDataSetChanged()
        listPeliculasView.adapter = adaptador

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelis_view)
        val txtDirector = findViewById<TextView>(R.id.txt_NombreDirectorPeli)
        nombreDirector = intent.getStringExtra("nombreDirector").toString()
        idDirector = intent.getIntExtra("idDirector", 0)
        txtDirector.text = nombreDirector
        onStart()
        val botonCrearPelicula = findViewById<Button>(R.id.btn_crear_Pelicula)
        botonCrearPelicula.setOnClickListener {
            abrirActividadConParametros(nombreDirector, idDirector, "", AnadirPelicula::class.java)
        }
        registerForContextMenu(listPeliculasView)
        val infoPelicula = findViewById<TextView>(R.id.txt_infoMovies)
        listPeliculasView.setOnItemClickListener { _, _, position, _ ->
            val peliSelect = listPeliculasView.getItemAtPosition(position) as Pelicula
            infoPelicula.text = peliSelect.imprimirDatosPelicula()
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

        BaseDatos.Tablas = SQLiteHelper(this)
        onStart()
        val peliculaSeleccionada = listPeliculasView.getItemAtPosition(posicionItemSeleccionado) as Pelicula
        tituloPeliSeleccionada = peliculaSeleccionada.titulo
        listPeliculasView.adapter = adaptador
        val cancelarClick = { _: DialogInterface, _: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }
        val eliminarClick = { _: DialogInterface, _: Int ->
            Log.i("bdd", "Titulo Movie: $tituloPeliSeleccionada")
            BaseDatos.Tablas!!.eliminarPelicula(tituloPeliSeleccionada)
            onStart()
            Toast.makeText(this, "Eliminada", Toast.LENGTH_SHORT).show()
        }
        return when (item.itemId) {
            R.id.menu_editar -> {
                abrirActividadConParametros(nombreDirector, idDirector, tituloPeliSeleccionada, AnadirPelicula::class.java)
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

    private fun abrirActividadConParametros(director: String, idiDirector: Int, titulo: String, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        intentExplicito.putExtra("idDirector", idiDirector)
        intentExplicito.putExtra("tituloPelicula", titulo)
        startActivity(intentExplicito)
    }
}