package com.example.examen01

class Director(var id: Int, var nombre: String, var nacionalidad: String, var nacimiento: String, var numMovies: Int, var oscar: Int) {

    override fun toString(): String {
        return nombre
    }

    fun imprimirDatosDirector(): String {
        var ganador = "no"
        if (oscar == 1) ganador = "si"
        return """
                Nombre: $nombre 
                Nacionalidad: $nacionalidad
                Fecha de Nacimiento: $nacimiento
                Películas Dirigidas: $numMovies
                Ganador de Óscar: $ganador
            """.trimIndent()
    }

}