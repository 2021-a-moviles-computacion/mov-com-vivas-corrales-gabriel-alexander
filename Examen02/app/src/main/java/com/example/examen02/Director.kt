package com.example.examen02

class Director(var id: String?=null,
               var nombre: String?=null,
               var nacionalidad: String?=null,
               var nacimiento: String?=null,
               var num_pelis: Int?=null,
               var oscar: Int?=null) {

    override fun toString(): String {
        return nombre!!
    }

    fun imprimirDatosDirector(): String {
        var ganador = "no"
        if (oscar == 1) ganador = "si"
        return """
                Nombre: $nombre 
                Nacionalidad: $nacionalidad
                Fecha de Nacimiento: $nacimiento
                Películas Dirigidas: $num_pelis
                Ganador de Óscar: $ganador
            """.trimIndent()
    }

}