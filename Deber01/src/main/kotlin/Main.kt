import java.io.File
import java.io.PrintWriter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant.now
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    //Creamos la primera entidad, si ya está creada simplemente te indica que el archivo ya existe
    val entidad1 = "Autores.txt"
    crearArchivo(entidad1)
    eliminarAutor(entidad1, "Dali")
    /*
    println("**********************BIBLIOTECA AMÉRICA**********************\n" +
            "INICIO DEL SISTEMA\n" +
            "Seleccione:\n" +
            "1.Autores\n" +
            "2.Libros\n" )
    when(validarEntero()){
        1->{
            println( "1.Registrar un autor\n" +
                    "2. Actualizar datos de un autor\n" +
                    "3. Eliminar autor\n")
            when(validarEntero()){
                1->{
                    registrarAutor(entidad1)
                }
                2->{
                    actualizarArchivos(entidad1, 1,"dali", "Ecuatoriana")
                }
                3->{

                }
            }
        }
        2-> {

        }
        else-> println("Opción no válida")
    }



    println("Ingresa un Boolean:")
    val booleano = input.nextBoolean()
    println("Ingresa Nombre Archivo:")
    val nombreArchivo = input.next()+".txt"
    val date = LocalDate.parse("2018-12-12")
    */

}

fun escribirArchivosTest(nombreArchivo: String) {
    val writer = PrintWriter(nombreArchivo)
    writer.append("Texto" + "\n")
    writer.append("Texto2" + "\n")
    writer.close()
}

fun leerArchivos(nombreArchivo: String) {
    val file = File(nombreArchivo)
    file.forEachLine { println(it) }
}

fun actualizarAutor(nombreArchivo: String, indice: Int, busqueda: String, cambio: String) {
    val detallesAutor = convertirArchivoEnList(nombreArchivo)
    detallesAutor.forEach { if (it[0].equals(busqueda, true)) it[indice] = cambio }
    escribirArchivo(nombreArchivo,detallesAutor)
}

fun eliminarAutor(nombreArchivo: String, busqueda: String) {
    var detallesAutor = convertirArchivoEnList(nombreArchivo)
    detallesAutor = detallesAutor.filter { !(it[0].equals(busqueda, true)) } as ArrayList<MutableList<String>>
    escribirArchivo(nombreArchivo,detallesAutor)

}

fun convertirArchivoEnList(nombreArchivo: String): ArrayList<MutableList<String>>{
    var detallesAutor = ArrayList<MutableList<String>>()
    File(nombreArchivo).forEachLine { detallesAutor.add(it.split(";") as MutableList<String>) }//Transformarmos el archivo en una lista
    return detallesAutor
}

fun escribirArchivo(nombreArchivo: String ,detallesAutor: ArrayList<MutableList<String>> ){
    val writer = PrintWriter(nombreArchivo)
    detallesAutor.forEach { itList ->
        itList.forEach {
            if (it == itList[itList.size - 1]) writer.append("$it\n") else writer.append("$it;")
        }
    }
    writer.close()
}

fun crearArchivo(nombreArchivo: String) {
    if (File(nombreArchivo).createNewFile()) println("$nombreArchivo se ha creado correctamente") else println("$nombreArchivo  ya existe")
}

fun registrarAutor(entidad1: String) {
    val colon = ";"
    val saltoLínea = "\n"
    println("Ingrese el nombre del autor:")
    val autor = ingresoPorTeclado().nextLine()
    println("Nacionalidad:")
    val nacionalidad = ingresoPorTeclado().nextLine()
    println("Fecha de nacimiento:")
    val date = ingresarDate()
    println("Numero libros publicados:")
    val numLibros = validarEntero()
    //Guardamos en el archivo
    File(entidad1).appendText("$autor$colon$nacionalidad$colon$date$colon$numLibros$saltoLínea")
}

fun ingresarDate(): Date {
    println("Ingrese fecha en formato dd/MM/yyyy:")
    val dateFormat = SimpleDateFormat("dd/MM/yyyy") //Formato de ingreso
    val date = ingresoPorTeclado().nextLine()
    dateFormat.isLenient = false //evito que fechas inválidas ( más de 30 días o 12 meses) sean aceptadas
    return try {
        if (dateFormat.parse(date).after(dateFormat.parse(LocalDateTime.now().toString()))) {
            dateFormat.parse(date)
        } else {
            println("Fecha inválida")
            ingresarDate()
        }
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
        ingresoPorTeclado().nextLine().toInt() //Verifica número entero
    } catch (e: java.lang.Exception) {
        println("Ingrese un valor entero")
        validarEntero()
    }
}

//Verificación si el String ingresado es double
fun validarDouble(): Double {
    return try {
        ingresoPorTeclado().nextLine().toDouble() //Verifica número double
    } catch (e: java.lang.Exception) {
        println("Ingrese un valor decimal")
        validarDouble()
    }
}

fun menu() {
    println()
}