package com.example.examen01

class Pelicula(
    var id: Int,
    var titulo: String,
    var anioEstreno: Int,
    var duracion: Int,
    var valoracion: Float,
    var genero: String,
    var director: Int
) {

    override fun toString(): String {
        return titulo
    }

    fun imprimirDatosPelicula(): String {
        return """
                Título: $titulo 
                Año de estreno: $anioEstreno
                Duración: $duracion minutos
                Valoración: $valoracion Estrellas
                Géneros: $genero
            """.trimIndent()
    }
}