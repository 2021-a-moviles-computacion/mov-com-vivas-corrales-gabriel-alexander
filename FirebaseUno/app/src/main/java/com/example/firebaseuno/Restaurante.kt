package com.example.firebaseuno

class Restaurante(val nombre: String? = null) {

    override fun toString(): String {
        return nombre!!
    }
}