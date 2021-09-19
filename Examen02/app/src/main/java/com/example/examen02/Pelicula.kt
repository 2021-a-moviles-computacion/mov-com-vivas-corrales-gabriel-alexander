package com.example.examen02

class Pelicula(
    var id: String?=null,
    var titulo: String?=null,
    var anioEstreno: Int?=null,
    var duracion: Int?=null,
    var valoracion: Float?=null,
    var genero: String?=null,
    var latitud: Double?=null,
    var longitud: Double?=null,
    var ubicacion: String?=null,
    var director: String?=null
) {

    override fun toString(): String {
        return titulo!!
    }

    fun imprimirDatosPelicula(): String {
        return """
                Título: $titulo 
                Año de estreno: $anioEstreno
                Duración: $duracion minutos
                Valoración: $valoracion Estrellas
                Géneros: $genero
                Ubicacion: $ubicacion
            """.trimIndent()
    }
}