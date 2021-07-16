package com.example.examen01

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteHelper(contexto: Context?) : SQLiteOpenHelper(contexto, "examen", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCrearTablaDirector =
            """
                CREATE TABLE DIRECTOR(ID_DIR INTEGER PRIMARY KEY AUTOINCREMENT,
                NOMBRE TEXT NOT NULL, NACIONALIDAD TEXT NOT NULL,
                FECHANACIMIENTO TEXT NOT NULL, NUMMOVIES INTEGER NOT NULL, 
                OSCAR INTEGER NOT NULL CHECK (OSCAR IN (0, 1)) 
                )""".trimIndent()

        val scriptCrearTablaPelicula =
            """
                CREATE TABLE PELICULA(ID_PELI INTEGER PRIMARY KEY AUTOINCREMENT,
                TITULO TEXT NOT NULL, ANIOESTRENO INTEGER NOT NULL, 
                DURACION INTEGER NOT NULL, VALORACION REAL NOT NULL, GENERO TEXT NOT NULL,
                DIRECTOR INTEGER NOT NULL,
                FOREIGN KEY (DIRECTOR) REFERENCES DIRECTOR (ID_DIR) 
                )
            """.trimIndent()


        db?.execSQL(scriptCrearTablaDirector)
        Log.i("bdd", "Creación Tabla Director")
        db?.execSQL(scriptCrearTablaPelicula)
        Log.i("bdd", "Creación Tabla Película")
        //        TODO("Not yet implemented")
    }


    fun crearDirector(
        nombre: String,
        nacionalidad: String,
        fechaNacimiento: String,
        numPelis: Int,
        oscar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("NOMBRE", nombre)
        valoresAGuardar.put("NACIONALIDAD", nacionalidad)
        valoresAGuardar.put("FECHANACIMIENTO", fechaNacimiento)
        valoresAGuardar.put("NUMMOVIES", numPelis)
        valoresAGuardar.put("OSCAR", oscar)

        val resultadoEscritura: Long = conexionEscritura.insert("DIRECTOR", null, valoresAGuardar)
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1

    }

    fun crearPelicula(
        titulo: String,
        anioEstreno: Int,
        duracion: Int,
        valoracion: Float,
        generos: String,
        director: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("TITULO", titulo)
        valoresAGuardar.put("ANIOESTRENO", anioEstreno)
        valoresAGuardar.put("DURACION", duracion)
        valoresAGuardar.put("VALORACION", valoracion)
        valoresAGuardar.put("GENERO", generos)
        valoresAGuardar.put("DIRECTOR", director)

        val resultadoEscritura: Long = conexionEscritura.insert("PELICULA", null, valoresAGuardar)
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1

    }

    fun consultarDirectorPorNombre(nombreDirector: String): Director {
        val scriptConsultarDirector = "SELECT * FROM DIRECTOR WHERE NOMBRE=\"$nombreDirector\""
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultarDirector, null)
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = Director(0, "", "", "", 0, 0)
        if (existeUsuario) {
            do {
                val id = resultadoConsultaLectura.getInt(0)//ID
                val nombre = resultadoConsultaLectura.getString(1)
                val nacionalidad = resultadoConsultaLectura.getString(2)
                val fechaNacimiento = resultadoConsultaLectura.getString(3)
                val numPelis = resultadoConsultaLectura.getInt(4)
                val oscar = resultadoConsultaLectura.getInt(5)

                usuarioEncontrado.id = id
                usuarioEncontrado.nombre = nombre
                usuarioEncontrado.nacionalidad = nacionalidad
                usuarioEncontrado.nacimiento = fechaNacimiento
                usuarioEncontrado.numMovies = numPelis
                usuarioEncontrado.oscar = oscar

            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }

    fun consultarTodosDirectores(): ArrayList<Director> {
        val scriptConsultarDirector = "SELECT * FROM DIRECTOR"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultarDirector, null)
        val directores = ArrayList<Director>()
        resultadoConsultaLectura.moveToFirst()
        while (!resultadoConsultaLectura.isAfterLast) {
            val director = Director(0, "", "", "", 0, 0)
            director.id = resultadoConsultaLectura.getInt(0)//ID
            director.nombre = resultadoConsultaLectura.getString(1)
            director.nacionalidad = resultadoConsultaLectura.getString(2)
            director.nacimiento = resultadoConsultaLectura.getString(3)
            director.numMovies = resultadoConsultaLectura.getInt(4)
            director.oscar = resultadoConsultaLectura.getInt(5)
            directores.add(director)
            resultadoConsultaLectura.moveToNext()
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return directores
    }

    fun consultarTodasPeliculasDeUnDirector(idDirector:Int): ArrayList<Pelicula> {
        val scriptConsultarPeliculas = "SELECT * FROM PELICULA WHERE DIRECTOR=$idDirector"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultarPeliculas, null)
        val peliculas = ArrayList<Pelicula>()
        resultadoConsultaLectura.moveToFirst()
        while (!resultadoConsultaLectura.isAfterLast) {
            val pelicula = Pelicula(0, "", 0, 0, 0f, "", 0)
            pelicula.id = resultadoConsultaLectura.getInt(0)//ID
            pelicula.titulo = resultadoConsultaLectura.getString(1)
            pelicula.anioEstreno = resultadoConsultaLectura.getInt(2)
            pelicula.duracion = resultadoConsultaLectura.getInt(3)
            pelicula.valoracion = resultadoConsultaLectura.getFloat(4)
            pelicula.genero = resultadoConsultaLectura.getString(5)
            pelicula.director = resultadoConsultaLectura.getInt(6)
            peliculas.add(pelicula)
            resultadoConsultaLectura.moveToNext()
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return peliculas
    }

    fun consultarPeliculaPorTitulo(tituloPelicula: String): Pelicula {
        val scriptConsultarPeliculas = "SELECT * FROM PELICULA WHERE TITULO=\"$tituloPelicula\""
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultarPeliculas, null)
        resultadoConsultaLectura.moveToFirst()
        val pelicula = Pelicula(0, "", 0, 0, 0f, "", 0)
        while (!resultadoConsultaLectura.isAfterLast) {
            pelicula.id = resultadoConsultaLectura.getInt(0)//ID
            pelicula.titulo = resultadoConsultaLectura.getString(1)
            pelicula.anioEstreno = resultadoConsultaLectura.getInt(2)
            pelicula.duracion = resultadoConsultaLectura.getInt(3)
            pelicula.valoracion = resultadoConsultaLectura.getFloat(4)
            pelicula.genero = resultadoConsultaLectura.getString(5)
            pelicula.director = resultadoConsultaLectura.getInt(6)
            resultadoConsultaLectura.moveToNext()
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return pelicula
    }


    fun eliminarDirector(nombre: String): Boolean {
        val conexionEscritura = writableDatabase
        val resultadoEliminacion =
            conexionEscritura.delete("DIRECTOR", "NOMBRE=?", arrayOf(nombre))
        conexionEscritura.close()
        return resultadoEliminacion != -1
    }

    fun eliminarPelicula(titulo: String): Boolean {
        val conexionEscritura = writableDatabase
        val resultadoEliminacion =
            conexionEscritura.delete("PELICULA", "TITULO=?", arrayOf(titulo))
        conexionEscritura.close()
        return resultadoEliminacion != -1
    }

    fun actualizarDirector(
        nombre: String,
        nacionalidad: String,
        fechaNacimiento: String,
        numPelis: Int,
        oscar: Int,
        idActualizar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("NOMBRE", nombre)
        valoresAActualizar.put("NACIONALIDAD", nacionalidad)
        valoresAActualizar.put("FECHANACIMIENTO", fechaNacimiento)
        valoresAActualizar.put("NUMMOVIES", numPelis)
        valoresAActualizar.put("OSCAR", oscar)
        val resultadoActualizacion = conexionEscritura.update(
            "DIRECTOR",
            valoresAActualizar,
            "ID_DIR=?",
            arrayOf(idActualizar.toString())
        )
        conexionEscritura.close()
        return resultadoActualizacion != -1
    }

    fun actualizarPelicula(
        titulo: String,
        anioEstreno: Int,
        duracion: Int,
        valoracion: Float,
        generos: String,
        idActualizar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("TITULO", titulo)
        valoresAActualizar.put("ANIOESTRENO", anioEstreno)
        valoresAActualizar.put("DURACION", duracion)
        valoresAActualizar.put("VALORACION", valoracion)
        valoresAActualizar.put("GENERO", generos)
        val resultadoActualizacion = conexionEscritura.update(
            "PELICULA",
            valoresAActualizar,
            "ID_PELI=?",
            arrayOf(idActualizar.toString())
        )
        conexionEscritura.close()
        return resultadoActualizacion != -1
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

}


