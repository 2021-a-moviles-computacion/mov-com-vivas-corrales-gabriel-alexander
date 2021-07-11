package com.example.examen01

class BaseDatosDirectorOR {
    companion object {
        val arrayDirectores = arrayListOf<Director>()
        init {
            arrayDirectores.add(Director("Quentin Tarantino",60))
            arrayDirectores.add(Director("Martin Scorsese",50))
            arrayDirectores.add(Director("Alfred Hitchcock",20))
        }
    }
}