import java.util.*

class Autor {
    val separador = "||"
    val saltoLínea = "\n"
    val autor: String
    val nacionalidad: String
    val date: Date
    val numLibros: Int
    val occiso: Boolean

    init {
        println("Ingrese el nombre del autor:")
        autor = validarTexto()
        println("Nacionalidad:")
        nacionalidad = validarTexto()
        println("Fecha de nacimiento:")
        date = ingresarDate()
        println("Numero libros publicados:")
        numLibros = validarEntero()
        println(
            "Occiso:\n" +
                    "1. Sí\n" +
                    "2. No\n" +
                    "Seleccione:"
        )
        occiso = validarBooleano()
    }

    fun registrarAutor(archivo1: Archivo) {
        //Verificamos si el autor no ha sido registrado previamente
        if (!archivo1.buscarRegistro(autor)) {
            //Guardamos en el archivo
            archivo1.escritorArchivo("$autor$separador$nacionalidad$separador$date$separador$numLibros$separador$occiso$saltoLínea")
            println("Autor registrado exitósamente")
        } else {
            println("El autor ya está registrado")
        }
    }
}