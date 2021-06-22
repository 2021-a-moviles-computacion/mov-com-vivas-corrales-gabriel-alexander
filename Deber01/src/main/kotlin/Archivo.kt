import java.io.File
import java.io.PrintWriter
import java.util.ArrayList

class Archivo(val nombreArchivo: String) {
    init {
        if (File(nombreArchivo).createNewFile()) println("$nombreArchivo se ha creado correctamente") else println("$nombreArchivo  ya existe")
    }

    fun leerArchivos() {
        println("-------------------------------------------------------------------------")
        val file = File(this.nombreArchivo)
        file.forEachLine { println(it) }
        println("-------------------------------------------------------------------------")
    }

    fun actualizarArchivo(indice: Int, busqueda: String, cambio: String) {
        val registro = convertirArchivoEnList()
        registro.forEach { if (it[0].equals(busqueda, true)) it[indice] = cambio }
        reescribirArchivo(registro)
    }

    fun eliminarRegistro(busqueda: String) {
        var registro = convertirArchivoEnList()
        registro = registro.filter { !(it[0].equals(busqueda, true)) } as ArrayList<MutableList<String>>
        reescribirArchivo(registro)
    }

    fun convertirArchivoEnList(): ArrayList<MutableList<String>> {
        var detallesAutor = ArrayList<MutableList<String>>()
        File(nombreArchivo).forEachLine { detallesAutor.add(it.split("||") as MutableList<String>) }//Transformarmos el archivo en una lista
        return detallesAutor
    }

    fun buscarRegistro(busqueda: String): Boolean {
        val registros = convertirArchivoEnList()
        return registros.any { it[0].equals(busqueda, ignoreCase = true) }
    }

    fun reescribirArchivo(registro: ArrayList<MutableList<String>>) {
        val writer = PrintWriter(nombreArchivo)
        registro.forEach { itList ->
            itList.forEach { if (it == itList[itList.size - 1]) writer.append("$it\n") else writer.append("$it||") }
        }
        writer.close()
    }

    fun escritorArchivo(texto: String) {
        File(nombreArchivo).appendText(texto)
    }

}