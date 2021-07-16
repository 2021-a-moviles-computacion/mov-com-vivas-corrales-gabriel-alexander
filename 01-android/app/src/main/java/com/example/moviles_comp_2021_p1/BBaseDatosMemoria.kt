package com.example.moviles_comp_2021_p1

class BBaseDatosMemoria {
    //Variables estáticas
    companion object {
        val arregloBEntrenador = arrayListOf<BEntrenador>()

        init {
            arregloBEntrenador.add(BEntrenador("Juan", "juan@com"))
            arregloBEntrenador.add(BEntrenador("ana", "ana@com"))
            arregloBEntrenador.add(BEntrenador("sofia", "sofia@com"))
        }
    }
}
