package grupo3
import Combinators._

import scala.util
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object ParsersTadp {

  type Entrada = String
  type Salida[+T] = (T, Entrada)
  type Parser[+T] = Entrada => Try[Salida[T]]
  type Punto = List[Double]
  type Puntos = List[Punto]
  type Triangulo = (String, Puntos)
  type Rectangulo = Triangulo
  type Radio = Double
  type Circulo = (String,(Punto, Radio))


  final case class ParserException(message: String = "Error de Parseo") extends Exception(message)

  val anyChar: Parser[Char] = try {
     regexMatcher(".".r, (input) => input(0))
  }

  val char: Char => Parser[Char] = char => anyChar.satisfies(_ == char)
  val digit: Parser[Char] = anyChar.satisfies(_.isDigit)

  val string: String => Parser[String] = string => try{ regexMatcher(("^" + string).r, (input) => input)}

  def regexMatcher[T](regex: Regex, func: String => T): Parser[T] = try {
    input => {
      val matched = (regex findFirstIn input).mkString
      if (!matched.equals("") && !"".equals(input))
        Try(func(matched), input.substring(matched.length) )
      else
        Failure(throw ParserException("No se pudo encontrar '" + regex + "' en '" + input + "'"));
    }
  }

  val integer: Parser[Int] = try {regexMatcher("^-?[0-9]+".r, Integer.parseInt)}

  val double: Parser[Double] = try {regexMatcher("^-?[0-9]+(\\.[0-9]+)?".r, java.lang.Double.parseDouble)}

  val parserPunto: Parser[Punto] = double.sepBy(string(" @ "))

  val parserPuntos: Parser[Puntos] = parserPunto.sepBy(string(", "))

  val parserTriangulo: Parser[Triangulo] = string("triangulo") <~ char('[') <> parserPuntos <~ char(']')

  val parserRectangulo: Parser[Rectangulo] = string("rectangulo") <~ char('[') <> parserPuntos <~ char(']')

  val parserCirculo: Parser[Circulo] = {
    string("circulo") <~ char('[') <> (parserPunto <~ string(", ") <> double) <~ char(']')
  }

}