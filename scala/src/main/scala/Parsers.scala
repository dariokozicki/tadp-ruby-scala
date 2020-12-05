package grupo3
import Combinators._
import grupo3.ParsersTadp.char

import scala.util
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object ParsersTadp {

  type Entrada = String
  type Salida[+T] = (T, Entrada)
  type Parser[+T] = Entrada => Try[Salida[T]]
  type Punto = List[Double]
  type RGB = Punto
  type CantidadRotacion = Double
  type CantidadTraslacion = (Double,Double)
  type FiguraPuntos = (String, List[Punto])
  type Triangulo = FiguraPuntos
  type Rectangulo = FiguraPuntos
  type Radio = Double
  type Circulo = (String,(Punto, Radio))
  type Figura = (String, Equals with Serializable)
  type Grupo = (String, List[Figura])
  type Color = (String, (RGB, Figura))
  type Rotacion = (String, (CantidadRotacion, Figura))
  type Traslacion = (String, (CantidadTraslacion, Figura))

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

  def parserSeparador(customChar: Char): Parser[((List[Char], Char), List[Char])] = (char(' ').*) <> char(customChar) <> (char(' ').*)

  val parserPunto: Parser[Punto] = double.sepBy(parserSeparador('@'))

  val parserPuntos: Parser[List[Punto]] = parserPunto.sepBy(parserSeparador(','))

  val parserTriangulo: Parser[Triangulo] = string("triangulo") <~ parserSeparador('[') <> parserPuntos <~ parserSeparador(']')

  val parserRectangulo: Parser[Rectangulo] =
    string("rectangulo") <~ parserSeparador('[') <> parserPuntos <~ parserSeparador(']')

  val parserCirculo: Parser[Circulo] = {
    string("circulo") <~ parserSeparador('[') <> (parserPunto <~ string(", ") <> double) <~ parserSeparador(']')
  }

  def parserFigura: Parser[Figura] = {
    parserCirculo <|> parserRectangulo <|> parserTriangulo
  }

  def parserRGB: Parser[RGB] = {
    double.sepBy(parserSeparador(','))
  }

  def parserGrupo: Parser[Grupo] = {
    entrada => {
      (string("grupo") <~ parserSeparador('(') <>
        (parserFigura <|> parserGrupo).sepBy(parserSeparador(','))  <~ parserSeparador(')'))(entrada)
    }
  }

  def parserColor: Parser[Color] = {
    entrada => {
      (string("color") <~ parserSeparador('[')
        <> (parserRGB <~ parserSeparador(']')
        <~ parserSeparador('(') <> ( parserFigura <|> parserGrupo )
        <~ parserSeparador(')')))(entrada)
    }
  }
}