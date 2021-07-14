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
        //db?.execSQL(scriptCrearTablaPelicula)
        //Log.i("bdd", "Creación Tabla Película")
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

    fun consultarUsuarioPorId(id: Int): Director {
        val scriptConsultarDirector = "SELECT * FROM DIRECTOR WHERE ID_DIR= $id"
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
        var indice = 0
        while (!resultadoConsultaLectura.isAfterLast){
                val usuarioEncontrado = Director(0, "", "", "", 0, 0)
                usuarioEncontrado.id = resultadoConsultaLectura.getInt(0)//ID
                usuarioEncontrado.nombre = resultadoConsultaLectura.getString(1)
                usuarioEncontrado.nacionalidad = resultadoConsultaLectura.getString(2)
                usuarioEncontrado.nacimiento = resultadoConsultaLectura.getString(3)
                usuarioEncontrado.numMovies = resultadoConsultaLectura.getInt(4)
                usuarioEncontrado.oscar = resultadoConsultaLectura.getInt(5)
                Log.i("bdd", "${usuarioEncontrado.toString()}")
                directores.add(usuarioEncontrado)
                resultadoConsultaLectura.moveToNext()
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        Log.i("bdd", "${directores.toString()}")
        return directores
    }


    fun eliminarDirector(nombre:String): Boolean {
        val conexionEscritura = writableDatabase
        val resultadoEliminacion =
            conexionEscritura.delete("DIRECTOR", "NOMBRE=?", arrayOf(nombre))
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


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

}


