package tadp

import org.scalatest.flatspec._
import org.scalatest.matchers._
import tadp.TADPDrawingApp.{FiguraAux, Grupos, grupo, triangulo}

class ImagenesSpec extends AnyFlatSpec with should.Matchers {
  it should "sumar 1 + 1" in {
    1 + 1 shouldEqual 2
  }

  it should "Triangulo" in {
    triangulo(List("200 @ 300", "300 @ 500","600 @ 1000"))

  }

  it should "Grupo Caso Base" in {
    val listaGrupo: Grupos[FiguraAux] = List(triangulo(List("200 @ 50", "101 @ 335", "299 @ 335")))
    val listaGrupoGrupoFigura: Unit = grupo(List(
      grupo(List(triangulo(List("200 @ 50", "101 @ 335", "299 @ 335")),
                triangulo(List("200 @ 50", "101 @ 335", "299 @ 335")))),
      grupo(List(triangulo(List("100 @ 20", "10 @ 35", "29 @ 33")),
                  triangulo(List("300 @ 50", "401 @ 335", "799 @ 335"))))))

    grupo(listaGrupo)

  }

}



