package com.example.examen02

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Director_View : AppCompatActivity() {
    var posicionItemSeleccionado = 0
    var nombreDirectorSeleccionado = ""
    var idDirectorSeleccionado = ""
    lateinit var directores: ArrayList<Director>
    lateinit var adaptador: ArrayAdapter<Director>
    lateinit var listDirectoresView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_view)
        listDirectoresView = findViewById(R.id.listView_Director)
        //-------------------------------------------------------
        val arregloDirectores = mutableListOf<Director>()
        val db = Firebase.firestore
        val referencia = db.collection("director")
        referencia.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val director = document.toObject(Director::class.java)
                director.id = document.id
                arregloDirectores.add(director)
                adaptador.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Log.i("firebase-firestore", "Error leyendo coleccion")
        }
        directores = arregloDirectores as ArrayList<Director>
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        //-------------------------------------------------

        listDirectoresView.adapter = adaptador
        val botonCrearDirector = findViewById<Button>(R.id.btn_crear_Director)
        val botonVerPelis = findViewById<Button>(R.id.btn_verPelis)
        botonVerPelis.isEnabled = false //Desactivo botón hasta seleccionar
        botonCrearDirector.setOnClickListener { abrirActividad(AnadirDirector::class.java) }
        registerForContextMenu(listDirectoresView)
        val infoDirector = findViewById<TextView>(R.id.txt_InfoDir)
        adaptador.notifyDataSetChanged()
        listDirectoresView.setOnItemClickListener { _, _, position, _ ->
            botonVerPelis.isEnabled = true
            val dirSelec = listDirectoresView.getItemAtPosition(position) as Director
            infoDirector.text = dirSelec.imprimirDatosDirector()
            botonVerPelis.setOnClickListener {
                abrirActividadConParametros(
                    dirSelec.nombre!!,
                    dirSelec.id!!,
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
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position

    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        adaptador.notifyDataSetChanged()
        val db = Firebase.firestore
        val referencia = db.collection("director")
        val directorSeleccionado = listDirectoresView.getItemAtPosition(posicionItemSeleccionado) as Director
        nombreDirectorSeleccionado = directorSeleccionado.nombre!!
        idDirectorSeleccionado = directorSeleccionado.id!!
        listDirectoresView.adapter = adaptador
        val cancelarClick = { _: DialogInterface, _: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }
        val eliminarClick = { _: DialogInterface, _: Int ->
            Log.i("FIREBASE", "Nombre director: $nombreDirectorSeleccionado")
            //AQUI DEBO ELIMINAR
            referencia.document(idDirectorSeleccionado)
                .delete()
                .addOnSuccessListener { Log.i("firebase", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.i("firebase", "Error deleting document", e) }
            directores.remove(directorSeleccionado)
            adaptador.notifyDataSetChanged()
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


    private fun abrirActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    private fun abrirActividadConParametros(director: String, idDirector: String, clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreDirector", director)
        intentExplicito.putExtra("idDirector", idDirector)
        startActivity(intentExplicito)
    }

}