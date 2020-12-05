package tadp

import grupo3.ParsersTadp.{ParserException, Salida, parserEntrada}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import Simplificadores._
import scala.util.Try

class SimplificadorTest extends AnyFlatSpec with should.Matchers {
  def testAssertVerdeYResultado[T](actualResultado: Try[Salida[T]], resultadoEsperado: Salida[T]): Unit = {
    actualResultado.get shouldBe resultadoEsperado
  }
  def testAssertFallo[T](actualResultado: => Try[Salida[T]]): Unit = {
    intercept[ParserException] { actualResultado.get }
  }

  it should "test simplificar color dentro de color" in {
    val color = "color[255,255,255](color[255,255,255](rectangulo[10 @ 20, 30 @ 40]))"
    val colorParsed = parserEntrada(color)
    val colorFixed = simplificar(colorParsed.get._1)
    assertResult(("color", (List(255.0,255.0,255.0), ("rectangulo", (List(List(10.0,20.0), List(30.0,40.0)))))))(colorFixed)
  }

  it should "test simplificar redundancia" in {
    val redund = "escala[1,1](traslacion[0,0](rotacion[0](triangulo[10 @ 20, 30 @ 40, 50 @ 60])))"
    val redundParsed = parserEntrada(redund)
    val redundFixed = simplificar((redundParsed.get._1))
    assertResult(("triangulo",(List(List(10.0,20.0), List(30.0,40.0), List(50.0,60.0)))))(redundFixed)
  }

  it should "test simplificar grupos" in {
    val grupoConColor = "grupo(color[255,255,255](circulo[10 @ 20, 30]),color[255,255,255](circulo[10 @ 20, 30]),color[255,255,255](circulo[10 @ 20, 30]))"
    val grupoParsed = parserEntrada(grupoConColor)
    val grupoFixed = simplificar(grupoParsed.get._1)
    assertResult(
      ("color",
        (List(255.0,255.0,255.0),
          ("grupo",
            List(
              ("circulo",(List(10.0,20.0), 30)),
              ("circulo",(List(10.0,20.0), 30)),
              ("circulo",(List(10.0,20.0), 30))
            )
          )
        )
      )
    )(grupoFixed)
  }

  it should "sumar las rotaciones" in {
    val rotaciones = "rotacion[300](rotacion[10](rectangulo[100 @ 200, 300 @ 400]))"
    val rotParsed = parserEntrada(rotaciones)
    val rotFixed = simplificar(rotParsed.get._1)
    assertResult(
      (
        "rotacion",
        (
          List(310.0),
          (
            "rectangulo",
            List(List(100.0,200.0),List(300.0,400.0))
          )
        )
      )
    )(rotFixed)
  }

  it should "multiplificar los factores de la escala" in {
    val escala = "escala[2,3](escala[3,5](circulo[0 @ 5, 10]))"
    val escalaParsed = parserEntrada(escala)
    val escalaFixed = simplificar(escalaParsed.get._1)
    assertResult(
      (
        "escala",
        (
          List(6.0,15.0),
          ("circulo", (List(0.0,5.0), 10.0))
        )
      )
    )(escalaFixed)
  }

  it should "sumar los factores de la traslacion" in {
    val escala = "traslacion[20,30](traslacion[30,50](circulo[0 @ 5, 10]))"
    val escalaParsed = parserEntrada(escala)
    val escalaFixed = simplificar(escalaParsed.get._1)
    assertResult(
      (
        "traslacion",
        (
          List(50.0,80.0),
          ("circulo", (List(0.0,5.0), 10.0))
        )
      )
    )(escalaFixed)
  }
}
