package tadp

import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter
import Combinators._
import grupo3.ParsersTadp._

import scala.util.Try

object TADPDrawingApp extends App {

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
  type Grupos[T] = List[T]


  //Grupos de tipos de parsers

  //AUXILIARES
  def parserPunto: Parser[Option[((Double, Double), (Double, Double))]] = double.sepBy(string(" @ ")).sepBy(string(", ")).opt
  def parserValor: Parser[(Double, Double)] = double.sepBy(string(", "))
  def parserInicioGrupo: Parser[Char] = char('(')
  def parserFinTransformacion: Parser[Char] = char(')')
  def parserInicioFigura: Parser[Char] = char('[')
  def parserFinFigura: Parser[Char] = char(']')
  def parserEntradaLista:  Parser[((Serializable, Any), (Serializable, Any))] = parserEntrada.sepBy(string(", "))

  //FIGURAS
  def parserTrianguloPuntos: Parser[((Option[((Double, Double), (Double, Double))], Option[((Double, Double), (Double, Double))]), Option[((Double, Double), (Double, Double))])] = ( parserPunto <> parserPunto)  <> (parserPunto <~ parserFinFigura)
  def parserTriangulo: Parser[(String, ((Option[((Double, Double), (Double, Double))], Option[((Double, Double), (Double, Double))]), Option[((Double, Double), (Double, Double))]))] = (string("triangulo")  <~ parserInicioFigura) <>  parserTrianguloPuntos
  def parserCirculoPuntos: Parser[(Option[((Double, Double), (Double, Double))], Int)] = parserPunto <> (integer <~ parserFinFigura)
  def parserCirculo: Parser[(String, (Option[((Double, Double), (Double, Double))], Int))] = (string("circulo") <~ parserInicioFigura) <>  parserCirculoPuntos
  def parserRectanguloPuntos: Parser[((Option[((Double, Double), (Double, Double))], Option[((Double, Double), (Double, Double))]), (Option[((Double, Double), (Double, Double))], Option[((Double, Double), (Double, Double))]))] =  ( parserPunto <> parserPunto)  <> ((parserPunto <> parserPunto) <~ parserFinFigura)
  def parserRectangulo: Parser[(String, ((Option[((Double, Double), (Double, Double))], Option[((Double, Double), (Double, Double))]), (Option[((Double, Double), (Double, Double))], Option[((Double, Double), (Double, Double))])))] = (string("rectangulo") <~ parserInicioFigura) <>  parserRectanguloPuntos

  //TRANSFORMACIONES
  def parserEscalaPuntos: Parser[((Double, Double), (Double, Double))] = (parserValor <> parserValor)  <~ parserFinFigura
  def parserEscala: Parser[(String, ((Double, Double), (Double, Double)))] = (string("escala") <~ parserInicioFigura) <> (parserEscalaPuntos <~ parserInicioGrupo )
  def parserRotacionPuntos: Parser[(Double, Double)] = (parserValor <~ parserFinFigura)
  def parserRotacion: Parser[(String, (Double, Double))] = (string("rotacion") <~ parserInicioFigura) <>  parserRotacionPuntos
  def parserTraslacion: Parser[(String, (Double, Double))] = (string("traslacion") <~ parserInicioFigura) <>  parserRotacionPuntos
  def parserColorPuntos: Parser[(((Double, Double), (Double, Double)), (Double, Double))] =  (parserValor <> parserValor) <> (parserValor <~ parserFinFigura)
  def parserColor: Parser[(String, (((Double, Double), (Double, Double)), (Double, Double)))] = (string("color") <~ parserInicioFigura) <> parserColorPuntos

  //Parser general para aplicar recursividad
  def parserFigura: Parser[(String, (Serializable, Any))] = (parserTriangulo <|> parserCirculo) <|> parserRectangulo
  def parserGrupo: Parser[((String, ((Serializable, Any), (Serializable, Any))), Char)] = (string("grupo") <~ parserInicioGrupo) <> parserEntradaLista <> parserFinTransformacion
  def parserTransformacion: Parser[(((String, (Any, Any)), ((Serializable, Any), (Serializable, Any))), Char)] = (parserColor <|> parserRotacion) <|> (parserTraslacion <|> parserEscala) <> parserEntradaLista <> parserFinTransformacion
  def parserEntrada: Parser[(Serializable, Any)] = (parserTransformacion <|> parserGrupo) <|> parserFigura

  def dibujarTriangulo(adaptador: TADPDrawingAdapter, puntos: List[Puntos] ): Unit ={
    adaptador.triangle(puntos.head,puntos(0),puntos(1)).end()
  }

  def triangulo: Figura = entrada => {
    val aux = new PuntoRegex(entrada, 3)
    val puntos: List[Puntos] = aux.parsear()
    Triangulo(puntos)
  }

  def dibujarFigurasContenedores(adaptador:TADPDrawingAdapter, grupo:List[Figuras]): TADPDrawingAdapter ={
    grupo.head match {
        case Triangulo(puntos)           => dibujarFigurasContenedores(adaptador.triangle(puntos.head,puntos(0),puntos(1)), grupo.tail)
        case Rectangulo(puntos)          => dibujarFigurasContenedores(adaptador.rectangle(puntos.head,puntos(0)),grupo.tail)
        case Circulo(puntos,radio)       => dibujarFigurasContenedores(adaptador.circle(puntos,radio),grupo.tail)
        case Grupo(figuras) => dibujarFigurasContenedores(adaptador, grupo.tail)
        case ColorTransformacion(r,g,b,grupo) => dibujarFigurasContenedores(adaptador.beginColor(Color.rgb(r, g, b)),grupo.tail).end()
        case Rotacion(grados,grupo) => dibujarFigurasContenedores(adaptador.beginRotate(grados),grupo.tail).end()
        case Escala(x,y,grupo) => dibujarFigurasContenedores(adaptador.beginScale(x,y),grupo.tail).end()
      }
  }

  //Tomo cabeza, le paso la cola de Figuras(por si es grupo o transformacion), y el adapter
  //Devuelvo la cola de la cual tomare la siguiente figura, y el adapter devuelto por la anterior
  //como se cuando hago el adapter.end para las transformaciones?
  def dibujar(adaptador:TADPDrawingAdapter, grupos: List[Figura]): Unit ={

    //grupos.fold( (adaptador, _) => _.)

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

  def parsearBloqueEntrada(entrada: String): Any ={
    //val resultParser = string("grupo(")(entrada)
    //print(resultParser)
    val resultadoParseoGeneral = parserEntrada.*(entrada)

    //("grupo", ( ("triangulo", (((23,34),(34,34)),(34,34))) ("rectangulo")  ) )

    return resultadoParseoGeneral;
  }
}





