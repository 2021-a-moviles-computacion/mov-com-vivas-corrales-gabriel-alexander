package com.example.firebaseuno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.AdapterView

class EOrdenes : AppCompatActivity() {
    var nombreproductoSeleccionado = ""
    var precioSeleccionado = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eordenes)
        val cabecera = findViewById<TextView>(R.id.txt_cabecera)
        cabecera.text = "PRODUCTO\t\t\t\t\tCANTIDAD\t\t\tPRECIO/U\t\t\tTOTAL"
        val spinnerRestaurantes = findViewById<Spinner>(R.id.sp_restaurantes)
        val spinnerProductos = findViewById<Spinner>(R.id.sp_producto)
        val productos: MutableList<Producto> = obtenerDatos("producto") as MutableList<Producto>
        val restaurantes: MutableList<Restaurante> = obtenerDatos("restaurante") as MutableList<Restaurante>
        val adapterProducto: ArrayAdapter<Producto> = ArrayAdapter<Producto>(this, android.R.layout.simple_spinner_item, productos)
        val adapterRestaurante: ArrayAdapter<Restaurante> = ArrayAdapter<Restaurante>(this, android.R.layout.simple_spinner_item, restaurantes)
        listener(adapterProducto, spinnerProductos, "producto")
        listener(adapterRestaurante, spinnerRestaurantes, "restaurante")
        val listViewProducto = findViewById<ListView>(R.id.lv_lista_productos)
        var totalTotal = 0.0
        val productosPedidos = arrayListOf<String>()
        val adaptador: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, productosPedidos)
        listViewProducto.adapter = adaptador
        val totalText = findViewById<TextView>(R.id.txt_mostrarTotal)
        val cantidad = findViewById<EditText>(R.id.et_cantidad_producto)
        val botonAdd = findViewById<Button>(R.id.btn_anadir_lista_producto)
        botonAdd.setOnClickListener {
            val cantidadValor = cantidad.text.toString()
            if (cantidadValor != "" && cantidadValor.toInt() > 0) {
                val cantidadNumber = cantidadValor.toInt()
                val total = precioSeleccionado * cantidadNumber
                val porducto1 =
                    "\t\t\t$nombreproductoSeleccionado\t\t\t\t\t\t\t\t\t\t\t\t\t$cantidadNumber\t\t\t\t\t\t\t\t$precioSeleccionado\t\t\t\t\t\t$total  "
                totalTotal += total
                totalText.text = totalTotal.toString()
                productosPedidos.add(porducto1)
                adaptador.notifyDataSetChanged()
                cantidad.text.clear()
            }
        }
    }

    private fun listener(adapter: ArrayAdapter<*>, spinner: Spinner, nombreClase: String) {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (nombreClase == "producto") {
                    val productoSeleccionado: Producto = parent?.getItemAtPosition(position) as Producto
                    nombreproductoSeleccionado = productoSeleccionado.nombre!!
                    precioSeleccionado = productoSeleccionado.precio!!
                } else {
                    val restauranteSeleccionado: Restaurante = parent?.getItemAtPosition(position) as Restaurante
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.i("firebase-firestore", "Item no seleccionado")
            }
        }
    }

    private fun obtenerDatos(nombreColeccion: String): MutableList<*> {
        val arregloProductos = mutableListOf<Producto>()
        val arregloRestaurantes = mutableListOf<Restaurante>()
        val db = Firebase.firestore
        val referencia = db.collection(nombreColeccion)
        return when (nombreColeccion) {
            "producto" -> {
                referencia.get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        val producto = document.toObject(Producto::class.java)
                        arregloProductos.add(producto)
                    }
                }.addOnFailureListener {
                    Log.i("firebase-firestore", "Error leyendo coleccion")
                }
                arregloProductos.add(Producto("", 0.0)) //Mauskeramienta misteriosa xd
                arregloProductos

            }
            else -> {
                referencia.get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        val restaurante = document.toObject(Restaurante::class.java)
                        arregloRestaurantes.add(restaurante)
                    }
                }.addOnFailureListener {
                    Log.i("firebase-firestore", "Error leyendo coleccion")
                }
                arregloRestaurantes.add(Restaurante("")) //Mauskeramienta misteriosa xd
                arregloRestaurantes
            }
        }
    }
}