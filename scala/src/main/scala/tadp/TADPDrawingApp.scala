package tadp

import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter

object TADPDrawingApp extends App {

  sealed trait Figuras
  sealed trait Transformaciones
  case class Triangulo(puntos: List[Puntos]) extends Figuras
  case class Rectangulo(puntos: List[Puntos]) extends Figuras
  case class Circulo(puntos: Puntos, radio: Int) extends Figuras
  case class ColorTransformacion(r:Int,G:Int,b:Int, grupo:List[Figuras]) extends Transformaciones
  case class Rotacion(grados:Double,grupo: List[Figuras]) extends Transformaciones
  case class Escala(x: Double, y:Double,grupo: List[Figuras]) extends Transformaciones
  case class Traslacion(x:Double,y:Double,grupo: List[Figuras]) extends Transformaciones
  case class Grupo(figuras: List[Figura]) extends Figuras

  type Puntos = (Double,Double)
  type Figura = (List[String] => FiguraAux)
  type FiguraAux = Figuras
  type Agrupacion = Figura
  type Grupos[T] = List[T]

  def triangulo: Figura = entrada => {
    val aux = new PuntoRegex(entrada, 3)
    val puntos: List[Puntos] = aux.parsear()
    TADPDrawingAdapter
      .forScreen { adapter =>
        //dibujarTriangulo(adapter,List((45,88),(56,675),(63,12)))
        adapter.beginColor(Color.rgb(23, 234, 35))
          .triangle((45,88),(56,675),(63,12)).end().beginColor(Color.rgb(43, 23, 12)).rectangle((343,443),(445,543)).end()
      }
    new Triangulo(puntos)
  }

  def dibujarConTransformacion(transformacion: Transformaciones) = {
    transformacion match {
      case ColorTransformacion(r,g,b,grupo) => TADPDrawingAdapter
        .forScreen { adapter =>
          val adaptador = adapter.beginColor(Color.rgb(r, g, b))
          dibujarGrupo(adaptador,grupo)
        }
      case Rotacion(grados,grupo) => TADPDrawingAdapter
        .forScreen { adapter =>
          val adaptador = adapter.beginRotate(grados)
          dibujarGrupo(adaptador,grupo)
        }
    }
  }

  def dibujarGrupo (adaptador:TADPDrawingAdapter, grupo: List[Figuras]): Unit ={
    for(nodo <- grupo)
      nodo match {
        case Triangulo(puntos)           => adaptador.triangle(puntos.head,puntos(0),puntos(1))
        case Rectangulo(puntos)          => adaptador.rectangle(puntos.head,puntos(0))
        case Circulo(puntos,radio)       => adaptador.circle(puntos,radio)
        case Grupo(figuras) => dibujarGrupo(adaptador,grupo)
      }
    adaptador.end()
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


