package com.example.examen01

class BaseDatosDirectorOR {
    companion object {
        val arrayDirectores = arrayListOf<Director>()
        init {
            arrayDirectores.add(Director(1,"Quentin Tarantino","a", "10/10/1980",2,1))
            arrayDirectores.add(Director(2,"Martin Scorsese","b", "10/10/1900",2,1))
            arrayDirectores.add(Director(3,"Alfred Hitchcock","v", "10/10/1910",2,1))
        }
    }
}