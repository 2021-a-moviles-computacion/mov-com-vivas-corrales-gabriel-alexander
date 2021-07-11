package com.example.examen01

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class Director_View : AppCompatActivity() {
    var posicionItemSeleccionado = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director_view)
        val directores = BaseDatosDirectorOR.arrayDirectores
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Directores)
        listDirectoresView.adapter = adaptador
        val botonCrearDirector = findViewById<Button>(R.id.btn_crear_Director)
        val botonVerPelis = findViewById<Button>(R.id.btn_verPelis)
        botonVerPelis.isEnabled = false //Desactivo botón hasta seleccionar
        botonVerPelis.setOnClickListener { abrirActividad(Pelis_View::class.java) }
        //botonCrearDirector.setOnClickListener { anadirDirectorAlListView(Director("Gabriel Vivas",20),directores,adaptador)  }
        botonCrearDirector.setOnClickListener { abrirActividad(AnadirDirector::class.java) }
        registerForContextMenu(listDirectoresView)
        val infoDirector = findViewById<TextView>(R.id.txt_InfoDir)
        listDirectoresView.setOnItemClickListener { parent, view, position, id ->
            botonVerPelis.isEnabled = true
            var dirSelec = listDirectoresView.getItemAtPosition(position) as Director
            infoDirector.setText(
                "Nombre: ${dirSelec.nombre} \n" +
                        "Edad: ${dirSelec.edad}"
            )
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
        val cancelarClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }
        val eliminarClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(this, android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }

        val directores = BaseDatosDirectorOR.arrayDirectores
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, directores)
        val listDirectoresView = findViewById<ListView>(R.id.listView_Directores)
        listDirectoresView.adapter = adaptador

        return when (item.itemId) {
            R.id.menu_editar -> {
                abrirActividad(AnadirDirector::class.java)
                return true
            }
            R.id.menu_eliminar -> {
                val advertencia = AlertDialog.Builder(this)
                advertencia.setTitle("Eliminar")
                advertencia.setMessage("Seguro de eliminar?")
                advertencia.setNegativeButton("Cancelar",DialogInterface.OnClickListener( function = cancelarClick ))
                advertencia.setPositiveButton("Eliminar",DialogInterface.OnClickListener( function = cancelarClick ))
                advertencia.show()
//                adapter = MyListAdapter(this)
//                lv = findViewById<View>(android.R.id.list) as ListView
//                lv.setAdapter(adapter)
//                lv.setOnItemClickListener(OnItemClickListener { a, v, position, id ->
//                    val adb: AlertDialog.Builder = Builder(this@MyActivity)
//                    adb.setTitle("Delete?")
//                    adb.setMessage("Are you sure you want to delete $position")
//                    adb.setNegativeButton("Cancel", null)
//                    adb.setPositiveButton("Ok", object : OnClickListener() {
//                        fun onClick(dialog: DialogInterface?, which: Int) {
//                            MyDataObject.remove(position)
//                            adapter.notifyDataSetChanged()
//                        }
//                    })
//                    adb.show()
//                })


                //BaseDatosDirector.arrayDirectores.removeAt(posicionItemSeleccionado)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("DATO", "agua")
    }

}