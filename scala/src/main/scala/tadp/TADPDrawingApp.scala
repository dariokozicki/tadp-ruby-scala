package tadp

import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter

object TADPDrawingApp extends App {

  // Codigo de ejemplo en el repo
  /*
  TADPDrawingAdapter
    .forScreen { adapter =>
      adapter
        .beginColor(Color.rgb(100, 100, 100))
        .rectangle((200, 200), (400, 400))
        .end()
    }

   */

  type Puntos = (Double,Double)
  type Figura = (List[String] => List[Puntos])
  type FiguraAux = List[Puntos]
  type Grupo = List[Figura]
  type Grupos[R] = List[R]

  def triangulo: Figura = entrada => {
    val aux = new PuntoRegex(entrada, 3)
    val puntos: List[Puntos] = aux.parsear()
    print(puntos.head.toString)
    TADPDrawingAdapter
      .forScreen { adapter =>
        adapter
          .beginColor(Color.rgb(100, 100, 100))
          .triangle(puntos.head,puntos(1),puntos(2))
          .end()
      }
    puntos
  }

  def dibujar(): Unit ={

  }

  def grupo[R](input: Grupos[R]): Unit = {

    input match {
      case grupo: List[Grupo] => {
        println("Entre a List[Grupo]")
      }
      case figura: List[Figura] => {
        println("Entre a List[Figura]")
        //dibujarFiguras(figura)
      }
    }
  }

  class PuntoRegex(input: List[String], cantCoordenadas: Int) {
    val coordenadaRegex = "([0-9]+ @ [0-9]+)".r
    val puntoRegex = "([0-9]+) @ ([0-9]+)".r

    def parsear(): List[Puntos] = try {
      var array: Array[Puntos] = new Array[Puntos](0)
      for (unElemento <- input) {
        for (patterMatch <- coordenadaRegex.findAllMatchIn(unElemento)) {
          val cantidadCoordenadas = patterMatch.groupCount
          print(cantidadCoordenadas)
          for (unPunto <- puntoRegex.findAllMatchIn(patterMatch.group(1))) {
            array :+= (unPunto.group(1).toDouble, unPunto.group(2).toDouble)
          }
        }
      }
      array.toList
    }
     catch {
      case error: Exception => throw error
    }
  }

}


