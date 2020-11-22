package tadp

import scalafx.scene.canvas.Canvas
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
  val appWidth = 1024
  val appHeight = 640

  val canvas = new Canvas(appWidth, appHeight)
  val adapterGlobal = TADPDrawingAdapter(canvas)

  def color(adapter:TADPDrawingAdapter, rojo:Int, verde: Int, azul:Int) =
    adapter.beginColor(Color.rgb(rojo,verde,azul));

  sealed trait Figuras
  case class Triangulo(puntos: List[Puntos]) extends Figuras
  case class Rectangulo(puntos: List[Puntos]) extends Figuras
  case class Circulo(puntos: List[Puntos]) extends Figuras
  //case class Grupo(figuras: List[Figura]) extends Figuras


  type Puntos = (Double,Double)
  type Figura = (List[String] => FiguraAux)
  type FiguraAux = Figuras
  type Grupo = List[Figura]
  type Grupos[R] = List[R]

  def dibujarTriangulo(adaptador: TADPDrawingAdapter, puntos:List[Puntos] ): TADPDrawingAdapter ={
    adaptador.triangle(puntos.head,puntos(0),puntos(1))
  }

  def dibujarRectangulo(adaptador: TADPDrawingAdapter, puntos:List[Puntos] ): TADPDrawingAdapter ={
    adaptador.rectangle(puntos.head,puntos(0))
  }


  def triangulo: Figura = entrada => {
    val aux = new PuntoRegex(entrada, 3)
    val puntos: List[Puntos] = aux.parsear()
    dibujar()
    new Triangulo(puntos)
  }

  def dibujarFigura(adaptador:TADPDrawingAdapter, figura:Figuras): TADPDrawingAdapter ={
    figura match{
      case Triangulo(puntos)           => adaptador.triangle(puntos.head,puntos(0),puntos(1))
      case Rectangulo(puntos)          => adaptador.rectangle(puntos.head,puntos(0))
    }
  }

  /*def dibujarGrupo[T] (adaptador:TADPDrawingAdapter,grupo: Grupo[T]): Unit ={
    for(nodo <- grupo)
      nodo match {
        //case Figura(puntos) => dibujarFigura(adaptador,figura)
        //case Grupo() => dibujarGrupo(adaptador,grupo)
      }
  }*/

  def dibujar(): Unit ={
    TADPDrawingAdapter
      .forScreen { adapter =>
        val adaptador = adapter.beginColor(Color.rgb(100, 100, 100))
        //dibujarTriangulo(adaptador,List((34,56),(54,24),(63,12)))
        //dibujarRectangulo(adaptador,List((34,56),(54,24)))
        dibujarFigura(adaptador, Triangulo(List((34,56),(54,24),(63,12))))
        dibujarFigura(adaptador, Rectangulo(List((50,100),(40,124))))
        adaptador.end()
          //dibujarFigura(adapter, Triangulo(List((34,56),(54,24),(63,12))))
        //dibujarFigura(adapter, Rectangulo(List((50,100),(40,124))))
        /*for(figura <- grupo)
          figura match {
            //case Circulo()             => dibujarCirculo(adapter,puntos)
            //case Grupo               => iterarGrupo(grupo)
          }*/

      }
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


