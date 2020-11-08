package tadp

import Parsers._
import org.scalatest.flatspec._
import org.scalatest.matchers._

class ExampleSpec extends AnyFlatSpec with should.Matchers {
  it should "sumar 1 + 1" in {
    1 + 1 shouldEqual 2
  }
}

class ParserTest extends AnyFlatSpec{

    //Test de parser char
  it should "Test de parser char con 'chau'" in{
    val texto: String = "chau"
    val caracter: Char = 'c'
    val result = char(caracter,texto)
    assert(result.resultado == caracter)
  }
  it should "Test de parser char con 'hola'" in{
    val texto: String = "hola"
    val caracter: Char = 'c'
    val result = char(caracter,texto)
    assert(result.resultado != caracter)
  }

      // Test de Parser anyChar
  it should "Test de parser anyChar con 'hola'" in{
    val texto: String = "hola"
    val result = anyChar(texto)
    assert(result.resultado == 'h')
  }
  it should "Test de parser anyChar con cadena vacia '' | Rompe con Parseo Exception" in{
    val texto: String = ""
    assertThrows[ParseoException]{
      anyChar(texto)
    }
  }

    // Test de Parser digit
    it should "Test de parser digit con '12Prueba'" in{
      val texto: String = "12Prueba"
      val result = digit(texto)
      assert(result.resultado == 1)
    }

    it should "Test de parser digit con cadena sin digitos 'hola' | Rompe con Parseo Exception" in{
      val texto: String = "hola"
      assertThrows[ParseoException]{
        digit(texto)
      }
    }


}