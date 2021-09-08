package com.example.firebaseuno

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DRestaurante : AppCompatActivity() {
    var query: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drestaurante)
        val botonCrearRestaurante = findViewById<Button>(R.id.btn_crear_restaurante)
        botonCrearRestaurante.setOnClickListener {
            crearRestaurante()
        }
        val botonDatosPrueba = findViewById<Button>(R.id.btn_datos_prueba)
        botonDatosPrueba.setOnClickListener {
            //crearDatosPrueba()
            transaccion()
        }
        val botonConsultar = findViewById<Button>(R.id.btn_consultar)
        botonConsultar.setOnClickListener {
            consultar()
        }

    }

    fun transaccion() {
        val db = Firebase.firestore
        val referenciaCities = db.collection("cities").document("SF")
        db.runTransaction {
            val documentoActual = it.get(referenciaCities)
            val nuevaPoblacion = documentoActual.getDouble("population")?.plus(1)
            it.update(referenciaCities, "population", nuevaPoblacion)
        }.addOnSuccessListener { Log.i("transaccion", "Transaccion completa") }
            .addOnFailureListener { Log.i("transaccion", "ERROR") }

    }

    fun crearRestaurante() {
        val editTextNombre = findViewById<EditText>(R.id.et_nombre_restaurante)
        val nuevoRestaurante = hashMapOf<String, Any>(
            "nombre" to editTextNombre.text.toString()
        )
        val db = Firebase.firestore
        val referencia = db.collection("restaurante")
        referencia.add(nuevoRestaurante).addOnSuccessListener {
            editTextNombre.text.clear()
        }.addOnFailureListener {
            Log.i("error", "error")
        }
    }

    fun crearDatosPrueba() {
        val db = Firebase.firestore
        val cities = db.collection("cities")

        val data1 = hashMapOf(
            "name" to "San Francisco",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 860000,
            "regions" to listOf("west_coast", "norcal")
        )
        cities.document("SF").set(data1)

        val data2 = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 3900000,
            "regions" to listOf("west_coast", "socal")
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
            "name" to "Washington D.C.",
            "state" to null,
            "country" to "USA",
            "capital" to true,
            "population" to 680000,
            "regions" to listOf("east_coast")
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
            "name" to "Tokyo",
            "state" to null,
            "country" to "Japan",
            "capital" to true,
            "population" to 9000000,
            "regions" to listOf("kanto", "honshu")
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to null,
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "regions" to listOf("jingjinji", "hebei")
        )
        cities.document("BJ").set(data5)
    }

    fun guardarQuery(documentSnapshot: QuerySnapshot, citiesRef: Query) {
        if (documentSnapshot.size() > 0) {
            val ultimoDocumento = documentSnapshot.documents[documentSnapshot.size() - 1]
            query = citiesRef.startAfter(ultimoDocumento)
        }
    }

    fun consultar() {
        val db = Firebase.firestore
        // Create a reference to the cities collection
        var citiesRef = db.collection("cities").orderBy("population").limit(2)//hacemos que solo nos retorne2
        var tarea: Task<QuerySnapshot>? = null
        if (query == null) {
            tarea = citiesRef.get()
        } else {
            tarea = query!!.get()
        }
        if (tarea != null) {
            tarea.addOnSuccessListener { documentSnapshot ->
                guardarQuery(documentSnapshot, citiesRef)
                for (document in documentSnapshot) {
                    Log.i("consultas", "${document.data}")
                }
            }.addOnFailureListener {
                Log.i("consultas", "ERROR FATAL")
            }
        }

/*
        //CONSULTA POR ID
        citiesRef.document("BJ").get().addOnSuccessListener {
            Log.i("consultas", "${it.data} ")
        }.addOnFailureListener {}

        //CONSULTA COMPLETA
        citiesRef.whereEqualTo("country", "China").get().addOnSuccessListener {
            Log.i("consultas", "${it.documents} ")
            for (ciudad in it) {
                Log.i("consultas", "${ciudad.data} ")
                Log.i("consultas", "${ciudad.id} ")
            }
        }.addOnFailureListener {}


        //buscar por dos o más elementos campo '=='array-contains

        citiesRef.whereEqualTo("capital", false)
            .whereArrayContainsAny("regions", arrayListOf("socal", "norcal")).get()
            .addOnSuccessListener {
                for (ciudad in it) {
                    Log.i("consultas", " == array-contains  ${ciudad.data} ")
                }
            }.addOnFailureListener {}

        citiesRef.whereEqualTo("capital",true)
            .whereGreaterThanOrEqualTo("population",1000000).get()
            .addOnSuccessListener {
                for (ciudad in it) {
                    Log.i("consultas", " == array-contains  ${ciudad.data} ")
                }
            }.addOnFailureListener {}

        citiesRef.whereEqualTo("capital", false)
            .whereLessThanOrEqualTo("population", 4000000)
            .orderBy("population", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (ciudad in it) {
                    Log.i("consultas", " == array-contains  ${ciudad.data} ")
                }
            }.addOnFailureListener {}
            */
    }
}