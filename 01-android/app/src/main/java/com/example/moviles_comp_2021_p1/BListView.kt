package com.example.moviles_comp_2021_p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

class BListView : AppCompatActivity() {
    var posicionItemSeleccionado =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)




        val arregloNumeros = BBaseDatosMemoria.arregloBEntrenador

                                                    //Layout (visual)
        val adaptador = ArrayAdapter( this,android.R.layout.simple_list_item_1,arregloNumeros)
        val listViewEjemplo = findViewById<ListView>(R.id.ltv_ejemplo)
        listViewEjemplo.adapter = adaptador

        val botonAnadirNumero = findViewById<Button>(R.id.btn_anadir_numero)
        botonAnadirNumero.setOnClickListener {
            anadirItemsAlListView(
                BEntrenador("tEST", "test@com"),
                arregloNumeros,adaptador)
                 }

        listViewEjemplo.setOnItemLongClickListener { parent, view, position, id ->
            Log.i("list-view", "Dio click $position")


            val builder = AlertDialog.Builder(this)
            builder.setTitle("Titulo")
            //builder.setMessage("Mensaje")
            val seleccionUsuario = booleanArrayOf(true,false,false)
            val opciones = resources.getStringArray(R.array.string_array_opciones_dialogo)

            builder.setMultiChoiceItems(opciones,seleccionUsuario,{dialog,which,isChecked-> Log.i("list-view","$which $isChecked")})


            builder.setPositiveButton("si", { dialog,wich->Log.i("list-view", "si")})
            builder.setNegativeButton("No", null)

            val dialogo= builder.create()
            dialogo.show()


            return@setOnItemLongClickListener true
        }

        registerForContextMenu(listViewEjemplo)
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
        val id = info.position
        posicionItemSeleccionado = id
        Log.i("list-view", "list view $posicionItemSeleccionado")
        Log.i("list-view", "Entrenador ${BBaseDatosMemoria.arregloBEntrenador[id]}")
    }





//    override fun registerForContextMenu(view: View?) {
//        super.registerForContextMenu(view)
//    }


    fun anadirItemsAlListView(valor:BEntrenador, arreglo:ArrayList<BEntrenador>, adaptador: ArrayAdapter<BEntrenador>){
        arreglo.add(valor)
        adaptador.notifyDataSetChanged()//actualiza interfaz
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        //return super.onContextItemSelected(item)
        return when (item?.itemId){
            //Editar
                R.id.mi_editar->{
                    Log.i("list-view", "Editar ${BBaseDatosMemoria.arregloBEntrenador[posicionItemSeleccionado]}")
                    return true
                }
                //Eliminar
            R.id.mi_eliminar->{
                Log.i("list-view", "Eliminar ${BBaseDatosMemoria.arregloBEntrenador[posicionItemSeleccionado]}")
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}