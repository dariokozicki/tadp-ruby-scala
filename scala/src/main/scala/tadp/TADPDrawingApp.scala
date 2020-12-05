package tadp

import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter
import Combinators._
import grupo3.ParsersTadp._

import scala.util.Try

object TADPDrawingApp extends App {
/*
  sealed trait Figuras
  sealed trait FigurasContenedores
  case class Triangulo(puntos: List[Puntos]) extends Figuras
  case class Rectangulo(puntos: List[Puntos]) extends Figuras
  case class Circulo(puntos: Puntos, radio: Int) extends Figuras
  case class ColorTransformacion(r:Int,G:Int,b:Int, grupo:List[Figuras]) extends Figuras
  case class Rotacion(grados:Double,grupo: List[Figuras]) extends Figuras
  case class Escala(x: Double, y:Double,grupo: List[Figuras]) extends Figuras
  case class Traslacion(x:Double,y:Double,grupo: List[Figuras]) extends Figuras
  case class Grupo(figuras: List[Figura]) extends Figuras

  type Puntos = (Double,Double)
  type Figura = (List[String] => FiguraAux)
  type DrawFigura = (TADPDrawingAdapter => Figura => TADPDrawingAdapter)
  type FiguraAux = Figuras
  type Agrupacion = Figura
  type Grupos[T] = List[T]*/

//  def dibujarTriangulo(adaptador: TADPDrawingAdapter, puntos: List[Puntos] ): Unit ={
//    adaptador.triangle(puntos.head,puntos.head,puntos(1)).end()
//  }

  def dibujarTriangulo(adaptador: TADPDrawingAdapter, puntos: List[Punto]): TADPDrawingAdapter ={
    adaptador.triangle(
      (puntos.head.head,puntos.head.last),
      (puntos(1).head, puntos(1).last),
      (puntos(2).head,puntos(2).last))
  }

  def dibujarRectangulo(adaptador: TADPDrawingAdapter, puntos: List[Punto]): TADPDrawingAdapter ={
    adaptador.rectangle(
      (puntos.head.head,puntos.head.last),
      (puntos.last.head,puntos.last.last))
  }

  def dibujarCirculo(adaptador: TADPDrawingAdapter, punto: Punto, radio: Radio): TADPDrawingAdapter = {
    adaptador.circle(
      (punto.head,punto.last),
      radio
    )
  }

  def dibujarFigurasContenedores(adaptador:TADPDrawingAdapter, grupo:List[Figura]): TADPDrawingAdapter ={
    if (grupo.isEmpty) return adaptador
    grupo.head match {
        case ("triangulo", puntos: List[Punto]) => dibujarFigurasContenedores(dibujarTriangulo(adaptador, puntos),grupo.tail)
        case ("rectangulo", puntos: List[Punto]) => dibujarFigurasContenedores(dibujarRectangulo(adaptador, puntos),grupo.tail)
        case ("circulo",(punto:Punto,radio: Radio)) => dibujarFigurasContenedores(dibujarCirculo(adaptador, punto, radio),grupo.tail)
        case ("grupo", figuras: List[Figura]) =>
          dibujarFigurasContenedores(adaptador, figuras)
          dibujarFigurasContenedores(adaptador, grupo.tail)
        case ("color",(rgbs: List[Double], grupoColor: Figura)) =>
          dibujarFigurasContenedores(adaptador.beginColor(Color.rgb(rgbs.head.toInt, rgbs(1).toInt, rgbs(2).toInt)),List(grupoColor)).`end`()
          dibujarFigurasContenedores(adaptador, grupo.tail)
        case ("rotacion",(grados: List[Double],grupoRotacion: Figura)) =>
          dibujarFigurasContenedores(adaptador.beginRotate(grados.head),List(grupoRotacion)).end()
          dibujarFigurasContenedores(adaptador, grupo.tail)
        case ("escala",(dims: List[Double],grupoEscala: Figura)) =>
          dibujarFigurasContenedores(adaptador.beginScale(dims.head,dims.last),List(grupoEscala)).end()
          dibujarFigurasContenedores(adaptador, grupo.tail)
        case _ => adaptador
      }
  }

  //Tomo cabeza, le paso la cola de Figuras(por si es grupo o transformacion), y el adapter
  //Devuelvo la cola de la cual tomare la siguiente figura, y el adapter devuelto por la anterior
  //como se cuando hago el adapter.end para las transformaciones?
  def dibujarScreen(entrada: String): Unit ={
    val parseado = parsearBloqueEntrada(entrada)
    TADPDrawingAdapter.forScreen {
      adapter => dibujarFigurasContenedores(adapter,parseado)
    }
  }

/*

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
  }*/
/*

  class PuntoRegex(input: List[String], cantCoordenadas: Int) {
    val coordenadaRegex = "[([0-9]+ @ [0-9]+)]".r

    val coordenadaTriangulo = "triangulo\\[([0-9]+\\s*@\\s*[0-9]+,?\\s*){3}\\]".r

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
*/

  def parsearBloqueEntrada(entrada: String): List[Figura] ={
    val result = parserEntrada(entrada)
    List(result.get._1)
  }
}





