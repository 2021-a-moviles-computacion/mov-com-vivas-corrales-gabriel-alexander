import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

fun main() {
    //Creamos las entidades, si ya están creadas simplemente te indica que el archivo ya existe
    val archivo1 = Archivo("Autores.txt")
    val archivo2 = Archivo("Libros.txt")
    menu(archivo1, archivo2)
}

fun menu(archivo1: Archivo, archivo2: Archivo) {
    println(
        "**********************LIBRERÍA AMÉRICA**********************\n" +
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
                    val autor= Autor()
                    autor.registrarAutor(archivo1)
                    menu(archivo1, archivo2)
                }
                2 -> {
                    println("Ingrese nombre del autor:")
                    val autor = validarTexto()
                    if (archivo1.buscarRegistro(autor)) {
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
                            5-> {
                                println(
                                    "Occiso:\n" +
                                            "1. Sí\n" +
                                            "2. No\n" +
                                            "Seleccione:"
                                )
                                cambio = validarBooleano().toString()
                            }
                            else -> {
                                println("Opción no válida")
                                menu(archivo1, archivo2)
                            }
                        }

                        archivo1.actualizarArchivo(indice - 1, autor, cambio)
                        println("Registro actualizado con éxito")
                        menu(archivo1, archivo2)
                    } else {
                        println("Autor no registrado")
                        menu(archivo1, archivo2)
                    }

                }
                3 -> {
                    println("Ingrese nombre del autor:")
                    val autor = validarTexto()
                    if (archivo1.buscarRegistro(autor)) {
                        archivo1.eliminarRegistro(autor)
                        println("Autor eliminado del registro exitósamente")
                        menu(archivo1, archivo2)
                    } else {
                        println("Autor no registrado")
                        menu(archivo1, archivo2)
                    }
                }
                4 -> {
                    println("-------------------------------------------------------------------------")
                    println("Nombre||Nacionalidad||Fecha Nacimiento||Lib.publicados")
                    archivo1.leerArchivos()
                    menu(archivo1, archivo2)
                }
                else -> {
                    println("Opción no válida")
                    menu(archivo1, archivo2)
                }
            }
        }
        2 -> {
            println(
                "1. Registrar libro\n" +
                        "2. Actualizar datos de un libro\n" +
                        "3. Eliminar libro\n" +
                        "4. Ver Registro\n" +
                        "Seleccione:"
            )
            when (validarEntero()) {
                1 -> {
                    val libro = Libro(archivo1,archivo2)
                    libro.registrarLibro(archivo2)
                    menu(archivo1, archivo2)
                }
                2 -> {
                    println("Ingrese nombre del libro:")
                    val libro = validarTexto()
                    if (archivo2.buscarRegistro(libro)) {
                        println(
                            "1.Título\n" +
                                    "2.Autor\n" +
                                    "3.Número de páginas\n" +
                                    "4.Año publicación\n" +
                                    "5.Copias Disponibles\n" +
                                    "6.Precio:\n" +
                                    "Seleccione cambio:"
                        )
                        val indice = validarEntero()
                        var cambio = ""
                        when (indice) {
                            1 -> {
                                println("Ingrese el nombre del libro:")
                                cambio = validarTexto()
                            }
                            2 -> {
                                println("Ingrese Autor:")
                                cambio = validarTexto()
                                if (!archivo1.buscarRegistro(cambio)) {
                                    println(
                                        "Autor no registrado, desea registrarlo?\n" +
                                                "1.Sí\n" +
                                                "2.No,Cancelar"
                                    )
                                    when (validarEntero()) {
                                        1 -> {
                                            val autor= Autor()
                                            autor.registrarAutor(archivo1)
                                        }
                                        2 -> {
                                            println("Cancelando registro")
                                            menu(archivo1, archivo2)
                                        }
                                        else -> {
                                            println("Opción no válida")
                                            menu(archivo1, archivo2)
                                        }
                                    }

                                }
                            }
                            3 -> {
                                println("Numero de páginas:")
                                cambio = validarEntero().toString()
                            }
                            4 -> {
                                println("Año publicación:")
                                cambio = validarEntero(2021).toString()

                            }
                            5 -> {
                                println(
                                    "Copias disponibles\n" +
                                            "1. Sí\n" +
                                            "2. No\n" +
                                            "Seleccione:"
                                )
                                cambio = validarBooleano().toString()
                            }
                            6 -> {
                                println("Precio:")
                                cambio = validarDouble().toString()
                            }
                            else -> {
                                println("Opción no válida")
                                menu(archivo1, archivo2)
                            }
                        }
                        archivo2.actualizarArchivo(indice - 1, libro, cambio)
                        println("Registro actualizado con éxito")
                        menu(archivo1, archivo2)
                    } else {
                        println("Libro no registrado")
                        menu(archivo1, archivo2)
                    }

                }
                3 -> {
                    println("Ingrese nombre del libro:")
                    val libro = validarTexto()
                    if (archivo2.buscarRegistro(libro)) {
                        archivo2.eliminarRegistro(libro)
                        println("Libro eliminado del registro exitósamente")
                        menu(archivo1, archivo2)
                    } else {
                        println("Libro no registrado")
                        menu(archivo1, archivo2)
                    }
                }
                4 -> {
                    println("-------------------------------------------------------------------------")
                    println("Título||Autor||Num.Pag||Año publ.||Disponible||Precio")
                    archivo2.leerArchivos()
                    menu(archivo1, archivo2)
                }
                else -> {
                    println("Opción no válida")
                    menu(archivo1, archivo2)
                }
            }
        }
        3 -> {
            println("Fin del sistema")
        }
        else -> {
            println("Opción no válida, seleccione nuevamente")
            menu(archivo1, archivo2)
        }
    }
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
        if (dateIngresado.before(Date.from(Instant.now()))) {//Fechas deben ser antes de fecha actual
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
fun validarEntero(limite: Int = 10000): Int {
    return try {
        val num = ingresoPorTeclado().nextLine().toInt() //Verifica número entero
        if (num in 1 until limite) {
            num
        } else {
            println("Ingrese nuevamente")
            validarEntero(limite)
        }
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
    return if (ingreso.matches("^[\\p{L} .'-]+$".toRegex())) {
        ingreso
    } else {
        println("Ingrese solo texto")
        validarTexto()
    }
}

