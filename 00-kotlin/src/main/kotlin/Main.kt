
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    println("Hola mundo") //El punto y coma es opcional
    var edadProfesor = 32
    //Duck typing
    // var edadProfesor: Int =32
    var sueldoProfesor = 1.23

    //Variables Mutables: Mutan , pueden ser Reasiganadas "="
    var edadCachorro: Int = 0
    edadCachorro = 1;
    edadCachorro = 2;
    edadCachorro = 3;

    //Variables inmutables:  No se pueden reasignar
    val numeroCedula = 14587246597
    // numeroCedula = 14587246597

    //Tipos de variables (JAVA)
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 's'
    val fechaNacimiento: Date = Date()


    //Condicionales
    if (true) {
        //verdadero

    } else {
        //falso
    }


    val estadoCivilWhen: String = "S"
    when (estadoCivilWhen) {
        ("S") -> {
            println("Acercarse")
        }
        "C" -> {
            println("Alejarse")
        }
        "Un" -> println("hablar")
        else -> println("no reconocido")
    }


    val coqueteo = if (estadoCivilWhen == "s") true else false


    imprimirNombre("gabriel")

    calcularSueldo(100.00)
    calcularSueldo(100.00, 14.00)
    calcularSueldo(100.00, 14.00, 25.00)

    //Named Parameters / Parámetros nombrados
    calcularSueldo( bonoEspecial = 15.00,sueldo = 150.00)
    calcularSueldo(tasa = 14.00, bonoEspecial = 30.00, sueldo =1000.00)

    //TIPOS Arreglos


    //Arreglo Estático
    val arregloEstatico: Array<Int> = arrayOf(1,2,3)
    //arregloEstatico.add(12) No podemos añadir
    val arregloDinamico: ArrayList<Int> = arrayListOf(1,2,3,4,5,6,7,8,9,10)
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)


    //Operadores: Sirven en arreglos estáticos y dinámicos
    //For each Nos retorna un Unit (es decir nada)
    //For each: iteramos un arreglo

    val respuestaForEach: Unit = arregloDinamico.forEach{
        valorActual: Int ->println("Valor actual: ${ valorActual}")
    }

    println(respuestaForEach)

    arregloDinamico.forEach{
        println("Valor actual: ${it}")
    }

    arregloDinamico.forEachIndexed{ indice: Int, valorActual: Int -> println("Valor ${valorActual} Indice ${indice}")}


    //MAP: Muta el arreglo( cambia el arreglo)
    //1. Enviamos el nuevo valor de la iteracion
    //2. Nos devuelve un NUEVO ARREGLO con valores modificados

    val respuestaMap: List<Double> = arregloDinamico.map {valorActual: Int ->
        return@map valorActual.toDouble()+100.00
    }
    println(respuestaMap)

    println(arregloDinamico.map{ it+15 })

    //FILTER -> Filtra el arreglo
    //1. Devuelve una expresion ( true or false)
    //2.Nuevo arreglo filtrado

    val respuestaFilter: List<Int> = arregloDinamico.filter{valorActual: Int -> val mayoresCinco: Boolean = valorActual >5
    return@filter mayoresCinco}
     println(respuestaFilter)

    println( arregloDinamico.filter { it< 5 })


    //OR -> ANY (ALGUNO CUMPLE?)
    //AND -> ALL ( TODOS CUMPLEN?)


    val respuestaAny:Boolean = arregloDinamico.any { valorActual: Int -> return@any(valorActual>5) }
    println(respuestaAny)// true
    val respuestaAll: Boolean = arregloDinamico.all { valorActual: Int -> return@all(valorActual>5) }
    println(respuestaAll)// false

    //REDUCE -> Valor acumulado
                                                        //Acc siempre inicia en cero                        //Lógica
    val respuestaReduce: Int = arregloDinamico.reduce { acumulado: Int, valorActual: Int -> return@reduce acumulado+valorActual  }
    println(respuestaReduce) //= 78

    val arregloDanio = arrayListOf<Int>(12,15,8,10)
    val respuestaReduceFold = arregloDanio.fold(100) { acumulado, valorActual -> return@fold acumulado-valorActual}
        println (respuestaReduceFold)

    println(arregloDanio.fold(100){total,item->total-item})

    val vidaActual: Double = arregloDinamico.map { it*2.3 }.filter { it>20 }.fold(100.00){acc,i->acc-i}.also { println(it) }
    println(arregloDinamico.map { it*2.3 })
    println(arregloDinamico.map { it*2.3 }.filter { it>20 })
    println(arregloDinamico.map { it*2.3 }.filter { it>20 }.fold(100.00){acc,i->acc-i})
    println("Valor vida actual ${vidaActual}")


    val ejemploUno = Suma(1,2)
    val ejemploDos = Suma(null,2)
    val ejemploTres = Suma(1,null)
    println(ejemploUno.sumar())
    println(ejemploDos.sumar())
    println(ejemploTres.sumar())

} //Fin bloque main


//FUNCIONES

//Unit es el void de Kotlin


                                                                //nullable
//Parámetro        requerido    , opcional (defecto),( al terminar en  "?" puede ser nula)
fun calcularSueldo(sueldo: Double, tasa: Double = 12.00, bonoEspecial: Double? = null): Double {
    if (bonoEspecial == null) {
        return sueldo * (100 / tasa)
    } else {
        return sueldo * (100 / tasa) + bonoEspecial
    }

}

abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int
    constructor(uno: Int, dos: Int){
        println("Inicializar")
        numeroDos=dos
        numeroUno=uno
    }
}

                            //PROPIEDAD
abstract class Numeros ( protected var numeroUno: Int,protected var numeroDos: Int)//Constructor Primario
{
    init {
        println("Inicializar")
    }
}


            //Constructor Primario
            //Parámetro Requerido       Constructor super
class Suma( uno:Int, dos: Int): Numeros(uno,dos){
    init {
        this.numeroUno
        this.numeroDos
    }
                //Segundo constructor
                            //Parametros        //LLamada constructor Primario
                constructor(uno: Int?, dos: Int):this(uno ?: 0, dos)

                //Tercer constructor
                                                         //if(dos==null) 0 else dos
                constructor(uno: Int, dos: Int?):this(uno, dos ?: 0)


                //public fun sumar():Int{ //public no es necesario

                fun sumar():Int{
                                 //this.numeroUno+this.numeroDos
                    val total: Int= numeroUno+numeroDos
                    agregarHistorial(total)
                    return total
                }

                //SINGLETON
                companion object{
                    val historialSumas = arrayListOf<Int>()
                    fun agregarHistorial( valorNuevaSuma: Int){
                        historialSumas.add(valorNuevaSuma)
                        println(historialSumas)
                    }
                }
}