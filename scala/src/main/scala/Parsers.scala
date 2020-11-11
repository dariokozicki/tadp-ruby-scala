package grupo3
import Combinators._

import scala.util.Try

object ParsersTadp {

  final case class ParserException(message: String = "Error de Parseo") extends Exception(message)

  type Entrada = String
  type Salida[+T] = (T, Entrada)
  type Parser[+T] = Entrada => Try[Salida[T]]

  val anyChar: Parser[Char] = {
    val ret: Parser[Char] = input => Try(input(0), input.substring(1))
    ret.parseoException
  }
  val char: Char => Parser[Char] = char => anyChar.condicion(_ == char)
  val digit: Parser[Char] = anyChar.condicion(_.isDigit)

  val stringExtendido: Parser[String] = {
    val ret: Parser[String] = input => Try(input.split(" ").head, input.split(" ").tail.mkString(" "))
    ret.parseoException
  }
  val string: String => Parser[String] = string => stringExtendido.condicion(_ == string)

  val integer: Parser[Int] = {
    val ret: Parser[Int] = input => Try(input.toInt, "")
    ret.parseoException
  }

  val double: Parser[Double] = {
    val ret: Parser[Double] = input => Try(input.toDouble, "")
    ret.parseoException
  }


}