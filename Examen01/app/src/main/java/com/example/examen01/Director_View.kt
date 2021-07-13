package com.example.examen01

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class Director_View : AppCompatActivity() {
    var posicionItemSeleccionado = 0

    override fun onStart() {
        super.onStart()
        BaseDatos.TablaDirector= SQLiteHelper(this)
        val directores = BaseDatos.TablaDirector!!.consultarTodosDirectores()
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Director)
        adaptador.notifyDataSetChanged()
        listDirectoresView.adapter = adaptador
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_view)

        BaseDatos.TablaDirector= SQLiteHelper(this)

        val directores = BaseDatos.TablaDirector!!.consultarTodosDirectores()
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Director)
        listDirectoresView.adapter = adaptador
        val botonCrearDirector = findViewById<Button>(R.id.btn_crear_Director)
        val botonVerPelis = findViewById<Button>(R.id.btn_verPelis)
        botonVerPelis.isEnabled = false //Desactivo botón hasta seleccionar
        //botonCrearDirector.setOnClickListener { anadirDirectorAlListView(Director("Gabriel Vivas",20),directores,adaptador)  }
        botonCrearDirector.setOnClickListener { abrirActividad(AnadirDirector::class.java) }
        registerForContextMenu(listDirectoresView)
        val infoDirector = findViewById<TextView>(R.id.txt_InfoDir)

        listDirectoresView.setOnItemClickListener { parent, view, position, id ->
            botonVerPelis.isEnabled = true
            val dirSelec = listDirectoresView.getItemAtPosition(position) as Director
            val datosDirector = """
                Nombre: ${dirSelec.nombre} 
                Nacionalidad: ${dirSelec.nacionalidad}
                Fecha de Nacimiento: ${dirSelec.nacimiento}
                Películas Dirigidas: ${dirSelec.numMovies}
                Ganador de Óscar: ${dirSelec.oscar}
            """.trimIndent()
            infoDirector.text = datosDirector
            botonVerPelis.setOnClickListener { abrirActividadConParametros(dirSelec.nombre,Pelis_View::class.java) }
            return@setOnItemClickListener
        }


    }


    fun anadirDirectorAlListView(
        director: Director,
        arreglo: ArrayList<Director>,
        adaptador: ArrayAdapter<Director>
    ) {
        arreglo.add(director)
        adaptador.notifyDataSetChanged() //Actualizamos interfaz
    }

    fun eliminarDirectorAlListView(
        indice: Int,
        arreglo: ArrayList<Director>,
        adaptador: ArrayAdapter<Director>
    ) {
        arreglo.removeAt(indice)
        adaptador.notifyDataSetChanged() //Actualizamos interfaz
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

        BaseDatos.TablaDirector= SQLiteHelper(this)

        val directores = BaseDatos.TablaDirector!!.consultarTodosDirectores()
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Director)
        listDirectoresView.adapter = adaptador
        val cancelarClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }
        val eliminarClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
            BaseDatos.TablaDirector!!.eliminarDirector(posicionItemSeleccionado)
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

    fun abrirActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    fun abrirActividadConParametros(director: String, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        startActivity(intentExplicito)
    }


}