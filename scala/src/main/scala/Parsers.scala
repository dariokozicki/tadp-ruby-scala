package grupo3
import Combinators._
import com.sun.tools.attach.VirtualMachine.list
import grupo3.ParsersTadp.char

import scala.util
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object ParsersTadp {

  type Entrada = String
  type Salida[+T] = (T, Entrada)
  type Parser[+T] = Entrada => Try[Salida[T]]
  type Punto = List[Double]
  type FiguraPuntos = (String, List[Punto])
  type Triangulo = FiguraPuntos
  type Rectangulo = FiguraPuntos
  type Radio = Double
  type Circulo = (String,(Punto, Radio))
  type Figura = (String, Equals with Serializable)
  type Grupo = (String, List[Figura])
  type UnidadTransformacion = List[Double]
  type Transformacion = (String,(UnidadTransformacion, Figura))

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

  def toTuple2(list: List[Double]): (Double, Double) = {
    list match {
      case List(a, b) => (a, b)
    }
  }

  def toTuple3(list: List[Double]): (Double, Double, Double) = {
    list match {
      case List(a, b, c) => (a, b, c)
    }
  }

  val integer: Parser[Int] = try {regexMatcher("^-?[0-9]+".r, Integer.parseInt)}

  val double: Parser[Double] = try {regexMatcher("^-?[0-9]+(\\.[0-9]+)?".r, java.lang.Double.parseDouble)}

  def parserSeparador(customChar: Char): Parser[((List[Char], Char), List[Char])] = (char(' ').*) <> char(customChar) <> (char(' ').*)

  val parserPunto: Parser[Punto] = double.sepBy(parserSeparador('@'))

  val parserPuntos: Parser[List[Punto]] = parserPunto.sepBy(parserSeparador(','))

  val parserTriangulo: Parser[Triangulo] = string("triangulo") <~ parserSeparador('[') <>
    parserPuntos.satisfies(list => list.length == 3) <~
    parserSeparador(']')

  val parserRectangulo: Parser[Rectangulo] =
    string("rectangulo") <~ parserSeparador('[') <> parserPuntos.satisfies(list => list.length == 2) <~ parserSeparador(']')

  val parserCirculo: Parser[Circulo] = {
    string("circulo") <~
      parserSeparador('[') <>
      ((parserPunto <~ parserSeparador(',')) <>
      double) <~ parserSeparador(']')
  }

  def parserFigura: Parser[Figura] = {
    parserCirculo <|> parserRectangulo <|> parserTriangulo
  }

  def parserUnidades: Parser[UnidadTransformacion] = {
    double.sepBy(parserSeparador(','))
  }

  def parserGrupo: Parser[Grupo] = {
    entrada => {
      (string("grupo") <~ parserSeparador('(') <>
        (parserEntrada).sepBy(parserSeparador(','))  <~ parserSeparador(')'))(entrada)
    }
  }

  def parserTransformacion(nombre: String, condicion: Punto => Boolean): String => Try[((String, (Punto, (String, Equals with Serializable))), Entrada)] = {
    entrada: String => {
      (string(nombre) <~ parserSeparador('[')
        <> (parserUnidades.satisfies(condicion) <~ parserSeparador(']')
        <~ parserSeparador('(') <> ( parserEntrada )
        <~ parserSeparador(')')))(entrada)
    }
  }

  def parserColor: Parser[Transformacion] = {
    parserTransformacion("color",list => list.length == 3 && list.forall(num => num >= 0 && num <= 255))
  }

  def parserRotacion: Parser[Transformacion] = {
    entrada: String => {
      (string("rotacion") <~ parserSeparador('[')
        <> (parserUnidades.satisfies(list => list.length == 1 ).map((angs => angs.map(angulo => angulo % 360))) <~ parserSeparador(']')
        <~ parserSeparador('(') <> ( parserEntrada )
        <~ parserSeparador(')')))(entrada)
    }
  }

  def parserTraslacion: Parser[Transformacion] = {
    parserTransformacion(("traslacion"), list => list.length == 2)
  }

  def parserEscala: Parser[Transformacion] = {
    parserTransformacion("escala", list => list.length == 2)
  }

  def parserEntrada: Parser[Figura] = {
    entrada: String => {
      (parserFigura <|> parserGrupo <|> parserRotacion <|> parserColor <|> parserTraslacion <|> parserEscala)(entrada)
    }
  }


}