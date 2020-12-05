package tadp

import grupo3.ParsersTadp.{ParserException, Salida, char, double, parserCirculo, parserColor, parserEntrada, parserGrupo, parserPunto, parserPuntos, parserRectangulo, parserRotacion, parserTriangulo, string}
import Combinators._
import org.scalatest.flatspec._
import org.scalatest.matchers._
import scalafx.scene.paint.Color
import tadp.TADPDrawingApp.{dibujarFigurasContenedores, dibujarScreen, parsearBloqueEntrada}
import tadp.internal.TADPDrawingAdapter

import scala.util.Try

class ImagenesSpec extends AnyFlatSpec with should.Matchers {
  def testAssertVerdeYResultado[T](actualResultado: Try[Salida[T]], resultadoEsperado: Salida[T]): Unit = {
    actualResultado.get shouldBe resultadoEsperado
  }
  def testAssertFallo[T](actualResultado: => Try[Salida[T]]): Unit = {
    intercept[ParserException] { actualResultado.get }
  }
  it should "sumar 1 + 1" in {
    1 + 1 shouldEqual 2
  }

  /*it should "Triangulo" in {
    triangulo(List("200 @ 300", "300 @ 500","600 @ 1000"))
  }*/

  it should "Grupo Caso Base" in {
    //val listaGrupo: Grupos[FiguraAux] = List(triangulo(List("200 @ 50", "101 @ 335", "299 @ 335")))
    val listaGrupoGrupoFigura = "grupo(grupo(color[23,23,34](triangulo[200 @ 50, 101 @ 335, 299 @ 335]),rectangulo[200 @ 50, 101 @ 335]),grupo(triangulo[100 @ 20, 10 @ 35, 29 @ 33],triangulo[300 @ 50, 401 @ 335, 799 @ 335]))"
    parsearBloqueEntrada(listaGrupoGrupoFigura)
    /*val listaGrupoTransformacion: Unit =  color(23,23,23)(grupo(List(
      grupo(List(triangulo(List("200 @ 50", "101 @ 335", "299 @ 335")),
        triangulo(List("200 @ 50", "101 @ 335", "299 @ 335")))),
      grupo(List(triangulo(List("100 @ 20", "10 @ 35", "29 @ 33")),
        triangulo(List("300 @ 50", "401 @ 335", "799 @ 335")))))))*/

    //grupo(listaGrupo)

  }

  it should "punto 10 @ 20" in {
    val punto = "10 @ 20"
    val puntoParseado = parserPunto(punto)
    testAssertVerdeYResultado(puntoParseado, (List(10.0,20.0), ""))
  }

  it should "puntos 10 @ 20, 30 @ 40, 50 @ 60" in {
    val puntos = "10 @ 20, 30 @ 40, 50 @ 60"
    val puntosParseados = parserPuntos(puntos)
    testAssertVerdeYResultado(puntosParseados, (List(List(10.0,20.0), List(30.0,40.0), List(50.0,60.0)), ""))
  }

  it should "triangulo loco" in {
    val triangulo = "triangulo[10 @ 20, 30 @ 40, 50 @ 60]"
    val trianguloParseado = parserTriangulo(triangulo)
    testAssertVerdeYResultado(trianguloParseado, (("triangulo",List(List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0))),""))
  }

  it should "triangulo invalido" in {
    val triangulo = "triangulo[10 @ 20, 30 @ 40, 50 @ 60, 70 @ 80]"
    val trianguloParseado = parserTriangulo(triangulo)
    testAssertFallo(trianguloParseado)
  }

  it should "rectangulo loco" in {
    val rectangulo = "rectangulo[10 @ 20, 30 @ 40]"
    val rectanguloParseado = parserRectangulo(rectangulo)
    testAssertVerdeYResultado(rectanguloParseado,(("rectangulo",List(List(10.0, 20.0), List(30.0, 40.0))),""))
  }

  it should "circulo loco" in {
    val circulo = "circulo[200 @ 350, 100]"
    val circuloParseado = parserCirculo(circulo)
    testAssertVerdeYResultado(circuloParseado, (("circulo",(List(200.0,350.0), 100.0)),""))
  }

  it should "circulo invalido" in {
    val circulo = "circulo[200 @ 350, 300 @ 400, 100]"
    val circuloParseado = parserCirculo(circulo)
    testAssertFallo(circuloParseado)
  }

 /* it should "identificar figura" in {
    val triangulo = "triangulo[10 @ 20, 30 @ 40, 50 @ 60]"
    val trianguloParseado = parserFigura(triangulo)
    testAssertVerdeYResultado(trianguloParseado, (("triangulo",List(List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0))),""))
  }*/

  it should "grupo loco" in {
    var grupo = "grupo(circulo[200 @ 350, 100], triangulo[10 @ 20, 30 @ 40, 50 @ 60], rectangulo[10 @ 20, 30 @ 40])"
    val grupoParseado = parserGrupo(grupo)
    testAssertVerdeYResultado(grupoParseado,
      (
        ("grupo",
          List(
            ("circulo",
              (List(200.0,350.0),
                100.0
              )
            ),
            ("triangulo",
              List(
                List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0)
              )
            ),
            ("rectangulo",
              List(
                List(10.0, 20.0), List(30.0, 40.0)
              )
            )
          )
        ),""))
  }

  it should "grupo anidado" in {
    var grupo = "grupo(circulo[200 @ 350, 100], grupo(triangulo[10 @ 20, 30 @ 40, 50 @ 60], rectangulo[10 @ 20, 30 @ 40]))"
    val grupoParseado = parserGrupo(grupo)
    testAssertVerdeYResultado(grupoParseado,
      (
        ("grupo",
          List(
            ("circulo",
              (List(200.0,350.0),
                100.0
              )
            ),
            ("grupo",
              List(
                ("triangulo",
                  List(
                    List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0)
                  )
                ),
                ("rectangulo",
                  List(
                    List(10.0, 20.0), List(30.0, 40.0)
                  )
                )
              )
            )
          )
        ),""))
  }

  it should "grupo anidado en otro grupo anidado" in {
      var grupo = "grupo(triangulo[10 @ 20, 30 @ 40, 50 @ 60], grupo(circulo[200 @ 350, 100], grupo(triangulo[10 @ 20, 30 @ 40, 50 @ 60], rectangulo[10 @ 20, 30 @ 40])))"
      val grupoParseado = parserGrupo(grupo)
      testAssertVerdeYResultado(grupoParseado,
        (
          ("grupo",
            List(
              ("triangulo",
                List(
                  List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0)
                )
              ),
              ("grupo",
                List(
                  ("circulo",
                    (List(200.0,350.0),
                      100.0
                    )
                  ),
                  ("grupo",
                    List(
                      ("triangulo",
                        List(
                          List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0)
                        )
                      ),
                      ("rectangulo",
                        List(
                          List(10.0, 20.0), List(30.0, 40.0)
                        )
                      )
                    )
                  )
                )
              )
            )
          ),""))
  }

  it should "Test de colores" in {
    val color = "color[60, 150, 200](grupo(triangulo[200   @  50, 101 @ 335, 299 @ 335], circulo[200 @ 350, 100]))"
    val colorParseado = parserColor(color)
    testAssertVerdeYResultado(colorParseado,
      (
        ("color",
        (
          List(60.0,150.0,200.0),
          (
            "grupo",
            List(
              (
                "triangulo",
                List(
                  List(200.0,50.0), List(101.0,335.0), List(299.0,335.0)
                )
              ),
              (
                "circulo",
                (
                  List(200.0,350.0),
                  100.0
                )
              )
            )
          )
        )
      ), ""))
  }

  it should "Test de rotaciones" in {
    val rotacion = "rotacion[370](rectangulo[300 @ 0, 500 @ 200])"
    val rotacionParseada = parserRotacion(rotacion)
    testAssertVerdeYResultado(rotacionParseada,
      (("rotacion", (List(10.0), ("rectangulo", List(List(300.0,0.0), List(500.0,200))))),
        ""
      )
    )
  }

  it should "Test de entrada" in {
    var grupo = "grupo(triangulo[10 @ 20, 30 @ 40, 50 @ 60], grupo(circulo[200 @ 350, 100], grupo(triangulo[10 @ 20, 30 @ 40, 50 @ 60], rectangulo[10 @ 20, 30 @ 40])))"
    val grupoParseado = parserEntrada(grupo)
    testAssertVerdeYResultado(grupoParseado,
      (
        ("grupo",
          List(
            ("triangulo",
              List(
                List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0)
              )
            ),
            ("grupo",
              List(
                ("circulo",
                  (List(200.0,350.0),
                    100.0
                  )
                ),
                ("grupo",
                  List(
                    ("triangulo",
                      List(
                        List(10.0, 20.0), List(30.0, 40.0), List(50.0, 60.0)
                      )
                    ),
                    ("rectangulo",
                      List(
                        List(10.0, 20.0), List(30.0, 40.0)
                      )
                    )
                  )
                )
              )
            )
          )
        ),""))
  }


}



