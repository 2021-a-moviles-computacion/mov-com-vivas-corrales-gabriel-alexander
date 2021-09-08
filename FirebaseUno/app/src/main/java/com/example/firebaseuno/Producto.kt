package com.example.firebaseuno

class Producto(val nombre: String? = null,val precio: Double?=null) {

    override fun toString(): String {
        return nombre!!
    }
}