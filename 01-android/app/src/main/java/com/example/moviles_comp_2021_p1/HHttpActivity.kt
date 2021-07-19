package com.example.moviles_comp_2021_p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class HHttpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hhttp)
        metodoGet()
    }

    fun metodoGet(){
        "https://jsonplaceholder.typicode.com/posts/1".httpGet().responseString { request, response, result ->
            when(result){
                is Result.Failure ->{
                    val error = result.getException()
                    Log.i("klaxon", "Error: $error")
                }
                is Result.Success ->{
                    val getString = result.get()
                    Log.i("klaxon", getString)

                }
            }
        }
    }
}