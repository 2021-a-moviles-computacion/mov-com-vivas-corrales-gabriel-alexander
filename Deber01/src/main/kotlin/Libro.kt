class Libro(archivo1: Archivo, archivo2: Archivo) {
    val separador = "||"
    val saltoLinea = "\n"
    val libro: String
    val autor: String
    val numPag: Int
    val año: Int
    val disponible: Boolean
    val precio: Double

    init {
        println("Ingrese el nombre del libro:")
        libro = validarTexto()
        println("Ingrese Autor:")
        autor = validarTexto()
        if (!archivo1.buscarRegistro(autor)) {
            println(
                "Autor no registrado, desea registrarlo?\n" +
                        "1.Sí\n" +
                        "2.No,Cancelar"
            )
            when (validarEntero()) {
                1 -> {
                    val registro = Autor()
                    registro.registrarAutor(archivo1)
                }
                2 -> {
                    println("Cancelando registro")
                    menu(archivo1, archivo2)
                }
                else -> {
                    println("Opción no válida, registre nuevamente")
                    registrarLibro(archivo2)
                }
            }

        }
        println("Numero de páginas:")
        numPag = validarEntero()
        println("Año publicación:")
        año = validarEntero(2021)
        println(
            "Copias disponibles\n" +
                    "1. Sí\n" +
                    "2. No\n" +
                    "Seleccione:"
        )
        disponible = validarBooleano()
        println("Precio:")
        precio = validarDouble()
    }

    fun registrarLibro(archivo2: Archivo) {
        //Verificamos si el libro no ha sido registrado previamente
        if (!archivo2.buscarRegistro(libro)) {
            //Guardamos en el archivo
            archivo2.escritorArchivo("$libro$separador$autor$separador$numPag$separador$año$separador$disponible$separador$precio$saltoLinea")
            println("Libro registrado exitósamente")
        } else {
            println("El libro ya está registrado")
        }
    }
}