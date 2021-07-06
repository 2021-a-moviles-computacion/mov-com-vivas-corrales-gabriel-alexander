package com.example.moviles_comp_2021_p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class ControlBase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_base)
        BBaseDatos.TablaUsuario= ESqliteHelperUsuario(this)
        val botonCrear = findViewById<Button>(R.id.btn_CREARBASE)
        val botonActualizar = findViewById<Button>(R.id.btn_actualizar)
        val botonEliminar = findViewById<Button>(R.id.btn_eliminar)
        val botonConsultar = findViewById<Button>(R.id.btn_Consultar)
        val nombre = findViewById<EditText>(R.id.txt_nombre)
        val descripcion = findViewById<EditText>(R.id.txt_descripcion)
        val idIngreso = findViewById<EditText>(R.id.txt_id3)
        botonCrear.setOnClickListener {
            if(BBaseDatos.TablaUsuario!=null){

            BBaseDatos.TablaUsuario!!.crearUsuarioFormulario(nombre.text.toString(),descripcion.text.toString())
                Log.i("bdd", "creacion usuario ${nombre.text} ")
            }

        }

        botonConsultar.setOnClickListener {
            if(BBaseDatos.TablaUsuario!=null){
                val usuario= BBaseDatos.TablaUsuario!!.consultarUsuarioPorId(idIngreso.text.toString().toInt())
                Log.i("bdd", "Consulta id: ${idIngreso.text} es el usuario ${usuario.nombre} desc: ${usuario.descripcion} ")
            }

        }


        botonActualizar.setOnClickListener {
            if(BBaseDatos.TablaUsuario!=null){
                //val id=1
                val actualizado = BBaseDatos.TablaUsuario!!.actualizarUsuarioFormulario(nombre.text.toString(),descripcion.text.toString(), idIngreso.text.toString().toInt())
                Log.i("bdd", "usuario id: ${idIngreso.text} estado actualizacion ${actualizado} ")
            }

        }

        botonEliminar.setOnClickListener {
            if(BBaseDatos.TablaUsuario!=null){
                //val id=1
                val eliminacion = BBaseDatos.TablaUsuario!!.eliminarUsuarioFormulario(idIngreso.text.toString().toInt())
                Log.i("bdd", "usuario id: ${idIngreso.text} estado eliminacion ${eliminacion} ")
            }

        }

    }

}