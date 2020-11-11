package tadp

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ExampleSpec extends AnyFlatSpec with should.Matchers {
  it should "sumar 1 + 1" in {
    1 + 1 shouldEqual 2
  }
}

class ParserTest2 extends AnyFlatSpec{
/*
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
  it should "Test de parser char con 'hola' con excepcion" in{
    val texto: String = "hola"
    val caracter: Char = 'c'
    assertThrows[ParseoException]{
      char(caracter,texto)
    }

  }
  it should "Test de parser char con aplicacion parcial 'hola' y excepcion" in{
    val texto: String = "hola"
    val caracter: Char = 'c'
    val result = char(caracter,_:String)
    assertThrows[ParseoException]{
      result.apply(texto)
    }
  }
  it should "Test de parser char con aplicacion parcial 'tadp'" in{
    val texto: String = "tadp"
    val caracter: Char = 't'
    val result = char(caracter,_:String)

    assert(result.apply(texto).resultado ==  caracter)
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

    // Test de Parser string
  it should "Test de parser string con 'hola', 'hola Mundo!'" in{
    val textoBuscado = "hola"
    val texto: String = "hola Mundo!"

    assert(string(textoBuscado,texto).resultado == "hola")
  }
  it should "Test de parser string con 'holgado', 'hola Mundo!'" in{
    val textoBuscado = "holgado"
    val texto: String = "hola Mundo!"
    assertThrows[ParseoException]{
      string(textoBuscado,texto)
    }
  }

    // Test de Parser Int
  it should "Test de parser integer con '12345'" in{
    val texto: String = "12345"
    assert(integer(texto).resultado == texto.toInt)
  }
  it should "Test de parser integer con '-98765'" in{
    val texto: String = "-98765"
    assert(integer(texto).resultado == texto.toInt)
  }

  // Test de Parser Double
  it should "Test de parser double con 1234" in{
    val numero: Int = 1234
    assert(double(numero).resultado == numero.toDouble)
  }

  it should "Primer test de Combinators OR con 'arbol' " in{
    val texto: String = "arbol"

    //val aob2: ResultParser = char2('a',_:String) <|> char2('a',_:String))

    //aob2 texto


//    assert(aob2.apply(texto,texto).resultado == 'a')
  }
 /* it should "Segundo test de Combinators OR con 'bort' " in{
    val texto: String = "bort"
    val aob2 = char('a',_:String) _root_.Parsers.<|> char('b',_:String)

    assert(aob2.apply(texto,texto).resultado == 'b')
  }
  it should "Tercer test de Combinators OR con 'casa' para ver Exception" in{
    val texto: String = "casa"
    val aob2 = char('a',_:String) _root_.Parsers.<|> char('b',_:String)

    assertThrows[ParseoException]{
      val aux = aob2.apply(texto,texto)
      print("prueba")
    }
  }
*/*/
}



