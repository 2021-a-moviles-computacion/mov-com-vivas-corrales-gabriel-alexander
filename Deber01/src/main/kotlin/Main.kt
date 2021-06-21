import java.io.File
import java.io.PrintWriter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant.now
import java.util.*

fun main() {
    //Creamos las entidades, si ya están creadas simplemente te indica que el archivo ya existe
    val entidad1 = "Autores.txt"
    val entidad2 = "Libros.txt"
    crearArchivo(entidad1)
    crearArchivo(entidad2)
    menu(entidad1, entidad2)
    /*val date = LocalDate.parse("2018-12-12") */
}


fun escribirArchivosTest(nombreArchivo: String) {
    val writer = PrintWriter(nombreArchivo)
    writer.append("Texto" + "\n")
    writer.append("Texto2" + "\n")
    writer.close()
}

fun leerArchivos(nombreArchivo: String) {
    println("-------------------------------------------------------------------------")
    val file = File(nombreArchivo)
    file.forEachLine { println(it) }
    println("-------------------------------------------------------------------------")
}

fun actualizarAutor(nombreArchivo: String, indice: Int, busqueda: String, cambio: String) {
    val detallesAutor = convertirArchivoEnList(nombreArchivo)
    detallesAutor.forEach { if (it[0].equals(busqueda, true)) it[indice] = cambio }
    escribirArchivo(nombreArchivo, detallesAutor)
}

fun eliminarAutor(nombreArchivo: String, busqueda: String) {
    var detallesAutor = convertirArchivoEnList(nombreArchivo)
    detallesAutor = detallesAutor.filter { !(it[0].equals(busqueda, true)) } as ArrayList<MutableList<String>>
    escribirArchivo(nombreArchivo, detallesAutor)
}

fun convertirArchivoEnList(nombreArchivo: String): ArrayList<MutableList<String>> {
    var detallesAutor = ArrayList<MutableList<String>>()
    File(nombreArchivo).forEachLine { detallesAutor.add(it.split("||") as MutableList<String>) }//Transformarmos el archivo en una lista
    return detallesAutor
}


fun buscarAutor(nombreArchivo: String, busqueda: String): Boolean {
    val detallesAutor = convertirArchivoEnList(nombreArchivo)
    return detallesAutor.filter { it[0].equals(busqueda, ignoreCase = true) }.isNotEmpty()
}


fun escribirArchivo(nombreArchivo: String, detallesAutor: ArrayList<MutableList<String>>) {
    val writer = PrintWriter(nombreArchivo)
    detallesAutor.forEach { itList ->
        itList.forEach {
            if (it == itList[itList.size - 1]) writer.append("$it\n") else writer.append("$it||")
        }
    }
    writer.close()
}

fun crearArchivo(nombreArchivo: String) {
    if (File(nombreArchivo).createNewFile()) println("$nombreArchivo se ha creado correctamente") else println("$nombreArchivo  ya existe")
}

fun registrarAutor(entidad1: String) {
    val separador = "||"
    val saltoLínea = "\n"
    println("Ingrese el nombre del autor:")
    val autor = validarTexto()
    println("Nacionalidad:")
    val nacionalidad = validarTexto()
    println("Fecha de nacimiento:")
    val date = ingresarDate()
    println("Numero libros publicados:")
    val numLibros = validarEntero()
    //Verificamos si el autor no ha sido registrado previamente
    if (!buscarAutor(entidad1, autor)) {
        //Guardamos en el archivo
        File(entidad1).appendText("$autor$separador$nacionalidad$separador$date$separador$numLibros$saltoLínea")
        println("Autor registrado exitósamente")
    } else {
        println("El autor ya está registrado")
    }
}

fun registrarLibro(entidad2: String) {
    val separador= "||"
    val saltoLínea = "\n"
    println("Ingrese el nombre del libro:")
    val libro = validarTexto()
    println("Ingrese Autor:")
    if (!buscarAutor(entidad2, validarTexto()))
        println(
            "Autor no registrado, desea registrarlo?\n" +
                    "1.Sí" +
                    "2.No,Cancelar"
        )
    when (validarEntero()) {
        1 -> {
            registrarAutor("Autores.txt")
        }
        2 -> {
            println("Cancelando registro")
        }
        else -> {
            println("Opción no válida, registre nuevamente")
        }
    }


    println("Numero de páginas:")
    val numLibros = validarEntero()
    println("Año publicación")
    val año = validarEntero()
    println(
        "Copias disponibles\n" +
                "1. Sí\n" +
                "2. No\n" +
                "Seleccione:\n"
    )
    val disponible = validarBooleano()
    println("Precio")
    val precio = validarDouble()
}

fun validarBooleano(): Boolean {
    when (validarEntero()) {
        1 -> {
            return true
        }
        2 -> {
            return false
        }
        else -> {
            println("Opción no válida")
            return validarBooleano()
        }
    }
}

fun ingresarDate(): Date {
    println("Ingrese fecha en formato dd/MM/yyyy:")
    val dateFormat = SimpleDateFormat("dd/MM/yyyy") //Formato de ingreso
    val date = ingresoPorTeclado().nextLine()
    dateFormat.isLenient = false //evito que fechas inválidas ( más de 30 días o 12 meses) sean aceptadas
    return try {
        val dateIngresado = dateFormat.parse(date)
        if (dateIngresado.before(Date.from(now()))) {//Fechas deben ser antes de fecha actual
            dateIngresado
        } else {
            println("Fecha inválida")
            ingresarDate()
        }
        return dateFormat.parse(date)
    } catch (e: ParseException) {
        println("Formato no válido")
        ingresarDate() //Recursividad hasta recibir fecha válida
    }
}

fun ingresoPorTeclado(): Scanner { //Ingreso teclado
    return Scanner(System.`in`)
}

//Verificación si el String ingresado es numérico
fun validarEntero(): Int {
    return try {
        val num = ingresoPorTeclado().nextLine().toInt() //Verifica número entero
        if (num > 0) num else validarEntero()
    } catch (e: java.lang.Exception) {
        println("Ingrese un valor entero mayor a cero")
        validarEntero()
    }

}

//Verificación si el String ingresado es double
fun validarDouble(): Double {
    return try {
        val num = ingresoPorTeclado().nextLine().toDouble() //Verifica número double
        if (num > 0) num else validarDouble()
    } catch (e: java.lang.Exception) {
        println("Ingrese un valor decimal mayor a cero")
        validarDouble()
    }
}

fun validarTexto(): String {
    val ingreso = ingresoPorTeclado().nextLine()
    if (ingreso.matches("^[\\p{L} .'-]+$".toRegex())) {
        return ingreso
    } else {
        println("Ingrese solo texto")
        return validarTexto()
    }
}

fun menu(entidad1: String, entidad2: String) {
    println(
        "**********************BIBLIOTECA AMÉRICA**********************\n" +
                "INICIO DEL SISTEMA\n" +
                "1.Autores\n" +
                "2.Libros\n" +
                "3.Salir del Sistema\n" +
                "Seleccione:"
    )
    when (validarEntero()) {
        1 -> {
            println(
                "1. Registrar un autor\n" +
                        "2. Actualizar datos de un autor\n" +
                        "3. Eliminar autor\n" +
                        "4. Ver Registro\n" +
                        "Seleccione:"
            )
            when (validarEntero()) {
                1 -> {
                    registrarAutor(entidad1)
                    menu(entidad1, entidad2)
                }
                2 -> {
                    println("Ingrese nombre del autor:")
                    val autor = validarTexto()
                    if (buscarAutor(entidad1, autor)) {
                        println(
                            "1.Nombre\n" +
                                    "2.Nacionalidad\n" +
                                    "3.Fecha de Nacimiento\n" +
                                    "4.Número de libros publicados\n" +
                                    "Seleccione cambio:"
                        )
                        val indice = validarEntero()
                        var cambio = ""
                        when (indice) {
                            1 -> {
                                println("Ingrese el nombre del autor:")
                                cambio = validarTexto()

                            }
                            2 -> {
                                println("Nacionalidad:")
                                cambio = validarTexto()

                            }
                            3 -> {
                                println("Fecha de nacimiento:")
                                cambio = ingresarDate().toString()

                            }
                            4 -> {
                                println("Numero libros publicados:")
                                cambio = validarEntero().toString()
                            }
                            else -> {
                                println("Opción no válida")
                                menu(entidad1, entidad2)
                            }
                        }
                        actualizarAutor(entidad1, indice-1, autor, cambio)
                        menu(entidad1,entidad2)
                    } else {
                        println("Autor no registrado")
                    }

                }
                3 -> {
                    println("Ingrese nombre del autor:")
                    val autor = validarTexto()
                    if (buscarAutor(entidad1, autor)) {
                        eliminarAutor(entidad1, autor)
                        println("Autor eliminado del registro exitósamente")
                    }else{
                        println("Autor no registrado")
                        menu(entidad1,entidad2)
                    }
                }
                4->{
                    println("-------------------------------------------------------------------------")
                    println("Nombre||Nacionalidad||Fecha Nacimiento||Lib.publicados")
                    leerArchivos(entidad1)
                    menu(entidad1,entidad2)
                }
                else -> {
                    println("Opción no válida")
                    menu(entidad1, entidad2)
                }
            }
        }
        2 -> {

        }
        3 -> {
            println("Fin del sistema")
        }
        else -> {
            println("Opción no válida, seleccione nuevamente")
            menu(entidad1, entidad2)
        }
    }
}