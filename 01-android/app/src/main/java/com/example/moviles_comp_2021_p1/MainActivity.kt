package com.example.moviles_comp_2021_p1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401
    val CODIGO_RESPUESTA_INTENT_IMPLICITO = 402


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (BBaseDatos.TablaUsuario !=null){
//            BBaseDatos.TablaUsuario.consultarUsuarioPorId()
//
//        }
        val botonIrACicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        val botonIrListView = findViewById<Button>(R.id.btn_ir_list_view)
        val botonIrIntent = findViewById<Button>(R.id.btn_ir_intent)
        val botonIrBASE = findViewById<Button>(R.id.btn_BDD)
        val botonIrRecyclerView = findViewById<Button>(R.id.btn_ir_recycler_view)
        val botonIrHTTP = findViewById<Button>(R.id.btn_ir_http)
        botonIrHTTP.setOnClickListener { abrirActividad(HHttpActivity::class.java) }

        botonIrRecyclerView.setOnClickListener { abrirActividad(GRecyclerView::class.java) }

        botonIrBASE.setOnClickListener { abrirActividad(ControlBase::class.java) }
        botonIrACicloVida.setOnClickListener { abrirActividad(ACicloVida::class.java) }
        botonIrListView.setOnClickListener { abrirActividad(BListView::class.java) }
        botonIrIntent.setOnClickListener {  abrirActividaConParametros(CIntentExplicitoParametros::class.java)}

        val botonAbrirIntentImplicito = findViewById<Button>(R.id.btn_ir_intent_implicito)
        botonAbrirIntentImplicito.setOnClickListener { val intentConRespuesta = Intent( Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intentConRespuesta,CODIGO_RESPUESTA_INTENT_IMPLICITO)
        }



    }

    fun abrirActividad(clase: Class<*>){
                                //clase
        val intentExplicito = Intent(
            this,
            clase
        )
        //puedo poner this opcional this.startActivity
        startActivity(intentExplicito)
    }

    fun abrirActividaConParametros(clase: Class<*>){
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombre", "gabo")
        intentExplicito.putExtra("apellido", "vivas")
        intentExplicito.putExtra("edad", 18)
        //intentExplicito.putExtra("entrenador",BEntrenador("sancho", "panza"))
        //startActivity(intentExplicito)

        startActivityForResult(intentExplicito,CODIGO_RESPUESTA_INTENT_EXPLICITO)

//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            when(it.resultCode){
//                Activity.RESULT_OK ->{
//                    //Ejecutar código OK
//                    it.data?.getStringExtra("nombremodificado")
//                    it.data?.getStringExtra("apellidomodificado")
//                    it.data?.getIntExtra("edadmodificado",0)
//                    it.data?.getParcelableExtra<BEntrenador>("entrenadormodificado")
//                }
//            }
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_RESPUESTA_INTENT_EXPLICITO ->{
                if(resultCode == RESULT_OK){
                    Log.i("intent-explicito", "Si actualizó datos")
                    if(data!=null){
                        val nombre = data.getStringExtra("nombremodificado")
                        val apellido = data.getStringExtra("apellidomodificado")
                        val edad = data.getIntExtra("edadmodificado",0)
                         val entrenador = data.getParcelableExtra<BEntrenador>("entrenadormodificado")

                        Log.i("intent_explicito", "$nombre")
                        Log.i("intent_explicito", "$apellido")
                        Log.i("intent_explicito", "$edad")
                        Log.i("intent_explicito", "$entrenador")

                    }
                }
            }
            CODIGO_RESPUESTA_INTENT_IMPLICITO ->{
                if(resultCode== RESULT_OK){
                    if(data!= null){
                        val uri: Uri = data.data!! //Me vale vrg que sea nulo
                        val cursor = contentResolver.query(uri,null,null,null,null,null)
                        cursor?.moveToFirst()
                        val indiceTelefono = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val telefono = cursor?.getString(indiceTelefono!!)
                        cursor?.close()
                        Log.i("resultado", "Telf: $telefono")
                    }
                }
            }
        }
    }
}