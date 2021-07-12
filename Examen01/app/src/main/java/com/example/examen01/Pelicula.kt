package com.example.examen01

class Pelicula (var id: Int, var titulo: String, var duracion:String, var genero:String, var valoracion: Int,var director: Director) {

    override fun toString(): String {
        return "$titulo"
    }
}