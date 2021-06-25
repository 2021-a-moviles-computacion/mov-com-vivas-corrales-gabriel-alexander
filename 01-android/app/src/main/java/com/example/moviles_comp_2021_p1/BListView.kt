package com.example.moviles_comp_2021_p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class BListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        val arregloNumeros = arrayListOf<Int>(1,2,3)
                                                    //Layout (visual)
        val adaptador = ArrayAdapter( this,android.R.layout.simple_list_item_1,arregloNumeros)
        val listViewEjemplo = findViewById<ListView>(R.id.ltv_ejemplo)
        listViewEjemplo.adapter = adaptador

        val botonAnadirNumero = findViewById<Button>(R.id.btn_anadir_numero)
        botonAnadirNumero.setOnClickListener { anadirItemsAlListView(5,arregloNumeros,adaptador) }




//        listViewEjemplo.setOnItemLongClickListener { parent, view, position, id ->
//            Log.i("list-view", "Dio click $position")
//            return@setOnItemLongClickListener true
//        }

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
    }


//    override fun registerForContextMenu(view: View?) {
//        super.registerForContextMenu(view)
//    }


    fun anadirItemsAlListView(valor:Int, arreglo:ArrayList<Int>, adaptador: ArrayAdapter<Int>){
        arreglo.add(valor)
        adaptador.notifyDataSetChanged()//actualiza interfaz
    }
}