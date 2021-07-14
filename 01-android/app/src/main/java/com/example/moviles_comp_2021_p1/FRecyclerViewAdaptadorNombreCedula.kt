package com.example.moviles_comp_2021_p1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FRecyclerViewAdaptadorNombreCedula(private val contexto:GRecyclerView,private val listaEntrenadores: List<BEntrenador>,
private val recyclerView: RecyclerView):RecyclerView.Adapter<FRecyclerViewAdaptadorNombreCedula.MyViewHolder>() {
    var numeroLikes = 0
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        val nombreTxt: TextView
        val cedulaTxt: TextView
        val likesTxt: TextView
        val accionBtn: Button
        var numeroLikes = 0

        init {
            nombreTxt= view.findViewById(R.id.tv_nombre)
            cedulaTxt= view.findViewById(R.id.tv_cedula)
            likesTxt = view.findViewById(R.id.tv_likes)
            accionBtn = view.findViewById(R.id.btn_dar_like)
            accionBtn.setOnClickListener {
                this.anadirLike()
            }
        }

        private fun anadirLike() {
            numeroLikes += 1
            likesTxt.text = numeroLikes.toString()
            contexto.aumentarTotalLikes()
        }
    }

    //Tamaño del arreglo
    override fun getItemCount(): Int {
        //Cuantos existen
        return  listaEntrenadores.size
    }

    //Setear datos cada iteracion del arreglo
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entrenador = listaEntrenadores[position]
        holder.nombreTxt.text = entrenador.nombre
        holder.cedulaTxt.text = entrenador.descripcion
        holder.accionBtn.text = "Like ${entrenador.nombre}"
        holder.likesTxt.text = "0"
    }

    //Setear layout a utilizar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_lista, //Definimos la vista del recycler view
        parent,false)
        return  MyViewHolder(itemView)
    }






}