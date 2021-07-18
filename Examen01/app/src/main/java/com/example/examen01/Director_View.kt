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
    var nombreDirectorSeleccionado = ""
    var idDirectorSeleccionado = 0
    lateinit var directores :ArrayList<Director>
    lateinit var adaptador: ArrayAdapter<Director>
    lateinit var listDirectoresView: ListView

    override fun onStart() {
        super.onStart()
        BaseDatos.Tablas = SQLiteHelper(this)
        directores = BaseDatos.Tablas!!.consultarTodosDirectores()
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        listDirectoresView = findViewById(R.id.listView_Director)
        adaptador.notifyDataSetChanged()
        listDirectoresView.adapter = adaptador
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_view)
        BaseDatos.Tablas = SQLiteHelper(this)
        onStart()
        val botonCrearDirector = findViewById<Button>(R.id.btn_crear_Director)
        val botonVerPelis = findViewById<Button>(R.id.btn_verPelis)
        botonVerPelis.isEnabled = false //Desactivo botón hasta seleccionar
        botonCrearDirector.setOnClickListener { abrirActividad(AnadirDirector::class.java) }
        registerForContextMenu(listDirectoresView)
        val infoDirector = findViewById<TextView>(R.id.txt_InfoDir)

        listDirectoresView.setOnItemClickListener { _, _, position, _ ->
            botonVerPelis.isEnabled = true
            val dirSelec = listDirectoresView.getItemAtPosition(position) as Director
            infoDirector.text = dirSelec.imprimirDatosDirector()
            botonVerPelis.setOnClickListener {
                abrirActividadConParametros(
                    dirSelec.nombre,
                    dirSelec.id,
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

        BaseDatos.Tablas = SQLiteHelper(this)
        onStart()
        val directorSeleccionado = listDirectoresView.getItemAtPosition(posicionItemSeleccionado) as Director
        nombreDirectorSeleccionado = directorSeleccionado.nombre
        idDirectorSeleccionado = directorSeleccionado.id
        listDirectoresView.adapter = adaptador
        val cancelarClick = { _: DialogInterface, _: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }
        val eliminarClick = { _: DialogInterface, _: Int ->
            Log.i("bdd", "Nombre director: $nombreDirectorSeleccionado")
            BaseDatos.Tablas!!.eliminarDirector(nombreDirectorSeleccionado)
            onStart()
            Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
        }
        return when (item.itemId) {
            R.id.menu_editar -> {
                abrirActividadConParametros(nombreDirectorSeleccionado, idDirectorSeleccionado, AnadirDirector::class.java)
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

    private fun abrirActividadConParametros(director: String, idDirector: Int, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        intentExplicito.putExtra("idDirector", idDirector)
        startActivity(intentExplicito)
    }


}