package grupo3
import Combinators._

import scala.util.matching.Regex
import scala.util.{Success, Try}

object ParsersTadp {

  type Entrada = String
  type Salida[+T] = (T, Entrada)
  type Parser[+T] = Entrada => Try[Salida[T]]

  final case class ParserException(message: String = "Error de Parseo") extends Exception(message)

  val anyChar: Parser[Char] = {
    val ret: Parser[Char] = input => Try(input(0), input.substring(1))
    ret.parseoException
  }

  val char: Char => Parser[Char] = char => anyChar.condicion(_ == char)
  val digit: Parser[Char] = anyChar.condicion(_.isDigit)

  val string: String => Parser[String] = string => iterarString(_,string)

  def regexMatcher[T](regex: Regex, func: String => T ): Parser[T] ={
    val ret: Parser[T] = input => {
      val matched = (regex findFirstIn input).mkString
      Try(func(matched), input.substring(matched.length) )
    }
    ret.parseoException
  }

  val integer: Parser[Int] = try{
    lazy val regex = "-?[0-9]+".r
    regexMatcher(regex, Integer.parseInt)
  }

  val double: Parser[Double] = {
    lazy val regex = "-?[0-9]+(\\.[0-9]+)?".r
    regexMatcher(regex, java.lang.Double.parseDouble)
  }
}