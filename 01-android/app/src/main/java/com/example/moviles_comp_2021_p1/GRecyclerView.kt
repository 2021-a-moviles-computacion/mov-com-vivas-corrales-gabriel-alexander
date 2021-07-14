package com.example.moviles_comp_2021_p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GRecyclerView : AppCompatActivity() {
    var totalLikes = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grecycler_view2)
        val listaEntrenador = arrayListOf<BEntrenador>()
        val ligaPokemon = DLiga( "premier", "Liga premier")
        listaEntrenador.add(BEntrenador("Gabo", "Vivas",ligaPokemon))
        listaEntrenador.add(BEntrenador("Alexander", "Corrales",ligaPokemon))
        val recyclerViewEntrenadores = findViewById<RecyclerView>(R.id.rv_entrenadores)
        iniciarRecyclerView(listaEntrenador,this,recyclerViewEntrenadores)
    }
    fun iniciarRecyclerView( lista: List<BEntrenador>,actividad: GRecyclerView, recyclerView: RecyclerView){
        val adaptador = FRecyclerViewAdaptadorNombreCedula(actividad,lista,recyclerView)
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator= androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(actividad)
        adaptador.notifyDataSetChanged()
    }
    fun aumentarTotalLikes(){
        totalLikes += 1
        val textView = findViewById<TextView>(R.id.tv_total_likes)
        textView.text = totalLikes.toString()
    }
}