package tadp

import grupo3.ParsersTadp._
import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import Combinators._
import scala.util.Try


class ParserTest extends AnyFlatSpec{

  def testAssertVerdeYResultado[T](actualResultado: Try[Salida[T]], resultadoEsperado: Salida[T]): Unit = {
    actualResultado.get shouldBe resultadoEsperado
  }
  def testAssertFallo[T](actualResultado: => Try[Salida[T]]): Unit = {
    intercept[ParserException] { actualResultado.get }
  }

  //Tests de parser char
  it should "Test de anychar" in {
    testAssertVerdeYResultado(anyChar("hola"), ('h', "ola"))
  }

  it should "Test de parser char con 'H' y '' " in{
    testAssertFallo(char('H')(""))
  }
  it should "Test de parser char con 'L' y el string M" in{
    val texto: String = "M"
    val caracter: Char = 'L'

    testAssertFallo(char(caracter)(texto))
  }
  it should "Test de parser char con 'h' con string h" in{
    val texto: String = "h"
    val caracter: Char = 'h'

    testAssertVerdeYResultado(char(caracter)(texto),('h',""))
  }
  it should "Test de parser char con 'A' y 'Barco' " in{
    val texto: String = "Barco"
    val caracter: Char = 'A'

    testAssertFallo(char(caracter)(texto))
  }
  it should "Test de parser char con 'H' y el string 'Hola'" in{
    val texto: String = "Hola"
    val caracter: Char = 'H'

    testAssertVerdeYResultado(char(caracter)(texto),('H',"ola"))
  }

  // Tests de anyChar
  it should "Test de anyChar con string vacio " in{
    testAssertFallo(anyChar(""))
  }
  it should "Test de anyChar con string de 1 caracter R " in{
    testAssertVerdeYResultado(anyChar("R"),('R',""))
  }
  it should "Test de anyChar con string de mas de 1 caracter Racing " in{
    testAssertVerdeYResultado(anyChar("Racing"),('R',"acing"))
  }

  // Tests de digit
  it should "Test de digit con string vacio " in{
    testAssertFallo(digit(""))
  }
  it should "Test de digit con dos digitos al final del string " in{
    testAssertFallo(digit("Racing22"))
  }
  it should "Test de digit con string 0 " in{
    testAssertVerdeYResultado(digit("0"),('0',""))
  }
  it should "Test de digit con un digito delante del string " in{
    testAssertVerdeYResultado(digit("1Racing"),('1',"Racing"))
  }

  // Test de String
  it should "Test de string buscando Hola, con string Hola Mundo! " in{
    testAssertVerdeYResultado(string("Hola")("Hola Mundo!"),("Hola"," Mundo!"))
  }

  it should "Test de string buscando Hola, con string HolaMundo! " in{
    testAssertVerdeYResultado(string("Hola")("HolaMundo!"),("Hola","Mundo!"))
  }
  it should "Test de string buscando Hola, con string Holgado " in{
    testAssertFallo(string("Hola")("Holgado"))
  }

  // Test de Integer
  it should "Test de integer con string vacio " in{
    testAssertFallo(integer(""))
  }
  it should "Test de integer con un string 1234" in{
    testAssertVerdeYResultado(integer("1234"),(1234,""))
  }
  it should "Test de integer con un string negativo -33" in{
    testAssertVerdeYResultado(integer("-33"),(-33,""))
  }

  // Tests de Double
  it should "Test de double con string vacio " in{
    testAssertFallo(double(""))
  }
  it should "Test de double con string 234.22 " in{
    testAssertVerdeYResultado(double("234.22"),(234.22,""))
  }
  it should "Test de double con string 234.22A " in{
    testAssertVerdeYResultado(double("234.22A"), (234.22, "A"))
  }
}



