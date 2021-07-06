package com.example.moviles_comp_2021_p1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf

class ESqliteHelperUsuario(contexto: Context?): SQLiteOpenHelper(contexto,"moviles", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCrearTablaUsuario=
            """
                CREATE TABLE USUARIO(id INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR(50),
                DESCRIPCION VARCHAR (50))
            """.trimIndent()
        Log.i("bdd", "Creacion tabla usuario")
        db?.execSQL(scriptCrearTablaUsuario)
    }

    fun crearUsuarioFormulario(nombre: String, descripcion: String):Boolean{
        val conexionEscritura= writableDatabase
        val valoresAGuardar=ContentValues()
        valoresAGuardar.put("NOMBRE",nombre)
        valoresAGuardar.put("DESCRIPCION", descripcion)
        val resultadoEscritura: Long= conexionEscritura.insert("USUARIO", null,valoresAGuardar)
        conexionEscritura.close()
        return if (resultadoEscritura.toInt()==-1) false else true

    }

    fun consultarUsuarioPorId(id: Int):EUsuarioBDD{
        val scriptConsultarUsuario = "SELECT * FROM USUARIO WHERE ID= $id"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultarUsuario,null)
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        //val arregloUsuario = arrayListOf<EUsuarioBDD>()
        val usuarioEncontrado = EUsuarioBDD(0,"", "")
        if(existeUsuario) {
            do {
                val id = resultadoConsultaLectura.getInt(0)//ID
                val nombre = resultadoConsultaLectura.getString(1)
                val descripcion = resultadoConsultaLectura.getString(2)

                if(id!=null){
                    usuarioEncontrado.id=id
                    usuarioEncontrado.nombre=nombre
                    usuarioEncontrado.descripcion= descripcion
                    //arregloUsuario.add(usuarioEncontrado)
                }
            }while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }

    fun eliminarUsuarioFormulario(id:Int): Boolean{
        val conexionEscritura= writableDatabase
        val resultadoEliminacion= conexionEscritura.delete("USUARIO","id=?", arrayOf(id.toString()))
        conexionEscritura.close()
        return resultadoEliminacion.toInt() != -1

    }

    fun actualizarUsuarioFormulario(nombre: String, descripcion: String,idActualizar:Int):Boolean{
        val conexionEscritura= writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion",descripcion)
        val resultadoActualizacion = conexionEscritura.update("USUARIO",valoresAActualizar, "id=?", arrayOf(idActualizar.toString()))
        conexionEscritura.close()
        //return if( resultadoActualizacion ==-1) false else true
        return resultadoActualizacion != -1
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

}
