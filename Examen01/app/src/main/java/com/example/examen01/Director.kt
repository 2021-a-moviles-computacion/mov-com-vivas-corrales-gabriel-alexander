package com.example.examen01

class Director(var id: Int, var nombre: String, var nacionalidad:String, var nacimiento:String, var numMovies: Int,var oscar: Int) {

    override fun toString(): String {
        return "$nombre"
    }
}