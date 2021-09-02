package com.example.firebaseuno

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.firebaseuno.dto.FirestoreUsuarioDto
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val CODIGO_INICIO_SESION = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val botonLogin = findViewById<Button>(R.id.btn_login)
        botonLogin.setOnClickListener {
            llamarLoginUsuario()
        }
        val botonLogout = findViewById<Button>(R.id.btn_logout)
        botonLogout.setOnClickListener {
            solicitarSalirAplicativo()
        }

        val botonProducto = findViewById<Button>(R.id.btn_producto)
        botonProducto.setOnClickListener {
            val intent = Intent(this,CProducto::class.java)
            startActivity(intent)
        }

        val botonRestaurante = findViewById<Button>(R.id.btn_restaurante)
        botonRestaurante.setOnClickListener {
            val intent = Intent(this,DRestaurante::class.java)
            startActivity(intent)
        }
        val botonPedido = findViewById<Button>(R.id.btn_pedido)
        botonPedido.setOnClickListener {
            val intent = Intent(this,EOrdenes::class.java)
            startActivity(intent)
        }

    }

    fun llamarLoginUsuario() {
        val providers = arrayListOf(
            //Lista de proveedores
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                ).build(), CODIGO_INICIO_SESION
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODIGO_INICIO_SESION -> {
                if (resultCode == Activity.RESULT_OK) {
                    val usuario: IdpResponse? = IdpResponse.fromResultIntent(data)
                    if (usuario != null) {
                        if (usuario.isNewUser) {
                            Log.i("firebase-login", "Nuevo usuario ")
                            registrarUsuarioPorPrimeraVez(usuario)
                        } else {
                            setearUsuarioFirebase()
                            Log.i("firebase-login", "Usuario antiguo")
                        }
                    }
                } else {
                    Log.i("firebase-login", "El usuario cancelo")
                }
            }
        }
    }

    fun registrarUsuarioPorPrimeraVez(usuario: IdpResponse) {
        val usuarioLogeado = FirebaseAuth.getInstance().currentUser
        if (usuario.email != null && usuarioLogeado != null) {
            val db = Firebase.firestore  //obtenemos referencia
            val rolesUsuario = arrayListOf("usuario") //creamos el arreglo de permisos
            val identificadorUsuario = usuario.email
            val nuevoUsuario = hashMapOf<String, Any>(
                "roles" to rolesUsuario,
                "uid" to usuarioLogeado.uid,
                "email" to identificadorUsuario.toString()
            )

            //Creacion de la coleccion Usuario
            db.collection("usuario")

                /* Firestore Define el Id
                .add(nuevoUsuario)
                */
                //Yo defino el identificador
                .document(identificadorUsuario.toString())//quiero poner de id el correo
                .set(nuevoUsuario)

                .addOnSuccessListener {
                    Log.i("firebase-firestore", "Se anadio a la base")
                    setearUsuarioFirebase()
                }.addOnFailureListener {
                    Log.i("firebase-firestore", "Error")
                }

        } else {
            Log.i("firebase-login", "Correo Malo")
        }


    }

    fun setearUsuarioFirebase() {
        val instanciaAuth = FirebaseAuth.getInstance()
        val usuarioLocal = instanciaAuth.currentUser
        if (usuarioLocal != null) {
            if (usuarioLocal.email != null) {
                val db = Firebase.firestore
                val referencia = db.collection("usuario").document(usuarioLocal.email.toString())
                referencia.get().addOnSuccessListener {
                    //Mi método añadiendo null a BUsuaurioFirebase
//                    val usuarioCargado = it.toObject(BUsuarioFirebase::class.java)
//                    BAuthUsuario.usuario=usuarioCargado
//                    Log.i("firebase-firestore", "${BAuthUsuario.usuario?.uid}")
                    val usuarioCargado: FirestoreUsuarioDto? = //dto = Data Transfer Object
                        // data class
                        it.toObject(FirestoreUsuarioDto::class.java)
                    if (usuarioCargado != null) {
                        BAuthUsuario.usuario = BUsuarioFirebase(
                            usuarioCargado.uid, usuarioCargado.email, usuarioCargado.roles
                        )
                        setearBienvenida()
                    }
                    Log.i("firebase-firestore", "Usuario Cargado")
                }
                    .addOnFailureListener {
                        Log.i("firebase-firestore", "Fallo cargar usuario")
                    }
            }
        }
    }

    fun solicitarSalirAplicativo(){
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            BAuthUsuario.usuario=null
            setearBienvenida()
        }
    }


    fun setearBienvenida(){
        val txtViewBienvenida = findViewById<TextView>(R.id.tv_bienvenida)
        val botonLogin = findViewById<Button>(R.id.btn_login)
        val botonLogout = findViewById<Button>(R.id.btn_logout)
        val botonProducto = findViewById<Button>(R.id.btn_producto)
        val botonRestaurante = findViewById<Button>(R.id.btn_restaurante)
        val botonPedido = findViewById<Button>(R.id.btn_pedido)
        if(BAuthUsuario.usuario !=null){
            txtViewBienvenida.text = "Bienvenido ${BAuthUsuario.usuario?.email}"
            botonLogin.visibility = View.INVISIBLE
            botonLogout.visibility = View.VISIBLE
            botonProducto.visibility = View.VISIBLE
            botonRestaurante.visibility = View.VISIBLE
            botonPedido.visibility = View.VISIBLE
        }else{
            txtViewBienvenida.text = "Ingresa AL APLICATIVO"
            botonLogin.visibility = View.VISIBLE
            botonLogout.visibility = View.INVISIBLE
            botonProducto.visibility = View.INVISIBLE
            botonRestaurante.visibility = View.INVISIBLE
            botonPedido.visibility = View.INVISIBLE
        }
    }
}