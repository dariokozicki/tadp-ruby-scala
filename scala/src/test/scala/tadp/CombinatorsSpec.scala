package tadp
import Combinators._
import grupo3.ParsersTadp.{Parser, ParserException, Salida, char}
import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import scala.util.Try

class CombinatorsTest extends AnyFlatSpec{
  def testAssertVerdeYResultado[T](actualResultado: Try[Salida[T]], resultadoEsperado: Salida[T]): Unit = {
    actualResultado.get shouldBe resultadoEsperado
  }
  def testAssertFallo[T](actualResultado: ⇒ Try[Salida[T]]): Unit = {
    intercept[ParserException] { actualResultado.get }
  }

  // Val de combinators inicial
    val aob: Parser[Char] = char('a') <|> char('b')

  it should "Test de Combinators OR con 'arbol' " in{
    testAssertVerdeYResultado(aob("arbol"),('a',"rbol"))
     }

  it should "Test de Combinators OR con string r " in{
    testAssertFallo(aob("r"))
  }
  it should "Test de Combinators OR con string vacio " in{
    testAssertFallo(aob(""))
  }
  it should "Test de Combinators OR con string bort " in{
    testAssertVerdeYResultado(aob("bort"),('b',"ort"))
  }
  it should "Test de Combinators OR con string baaart " in{
    testAssertVerdeYResultado(aob("baaart"),('b',"aaart"))
  }
}


