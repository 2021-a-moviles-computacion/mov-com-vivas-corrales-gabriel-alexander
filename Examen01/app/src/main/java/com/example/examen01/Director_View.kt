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
import androidx.constraintlayout.solver.widgets.analyzer.Direct


class Director_View : AppCompatActivity() {
    var posicionItemSeleccionado = 0
    var nombreDirectorSeleccionado = ""

    override fun onStart() {
        super.onStart()
        BaseDatos.TablaDirector = SQLiteHelper(this)
        val directores = BaseDatos.TablaDirector!!.consultarTodosDirectores()
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Director)
        adaptador.notifyDataSetChanged()
        listDirectoresView.adapter = adaptador
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_view)

        BaseDatos.TablaDirector = SQLiteHelper(this)

        val directores = BaseDatos.TablaDirector!!.consultarTodosDirectores()
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Director)
        listDirectoresView.adapter = adaptador
        val botonCrearDirector = findViewById<Button>(R.id.btn_crear_Director)
        val botonVerPelis = findViewById<Button>(R.id.btn_verPelis)
        botonVerPelis.isEnabled = false //Desactivo botón hasta seleccionar
        botonCrearDirector.setOnClickListener { abrirActividad(AnadirDirector::class.java) }
        registerForContextMenu(listDirectoresView)
        val infoDirector = findViewById<TextView>(R.id.txt_InfoDir)

        listDirectoresView.setOnItemClickListener { parent, view, position, id ->
            botonVerPelis.isEnabled = true
            val dirSelec = listDirectoresView.getItemAtPosition(position) as Director
            infoDirector.text = dirSelec.imprimirDatosDirector()
            botonVerPelis.setOnClickListener {
                abrirActividadConParametros(
                    dirSelec.nombre,
                    Pelis_View::class.java
                )
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

        BaseDatos.TablaDirector = SQLiteHelper(this)

        val directores = BaseDatos.TablaDirector!!.consultarTodosDirectores()
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Director)
        val directorSeleccionado = listDirectoresView.getItemAtPosition(posicionItemSeleccionado) as Director
        nombreDirectorSeleccionado = directorSeleccionado.nombre
        listDirectoresView.adapter = adaptador
        val cancelarClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }
        val eliminarClick = { dialog: DialogInterface, which: Int ->
            Log.i("bdd", "Nombre director: $nombreDirectorSeleccionado")
            BaseDatos.TablaDirector!!.eliminarDirector(nombreDirectorSeleccionado)
            onStart()
            Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
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