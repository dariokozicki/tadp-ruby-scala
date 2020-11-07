import com.sun.net.httpserver.Authenticator.Success

import scala.util.Try


package object Parsers {

  //type F = List[Any] => List[Any]

  case class ResultParser(sinConsumir: String, val resultado: Any) {
    //def resultad[T]() = resultado
  }

  class ParseoException(val parser: ResultParser) extends RuntimeException

  //class modificadores(primero: Parser, segundo: Parser) extends Parser //para modificadores podria utilizar Implicit Class

  def <|>(primerParser: (String) => ResultParser , segundoParser: (String) => ResultParser, texto: String): ResultParser = {
    if(primerParser.apply(texto).resultado != null) primerParser.apply(texto)
    else segundoParser.apply(texto)
  }


  def Failure(parser: ResultParser, error: Exception): ResultParser = {
    ResultParser(parser.sinConsumir, null)
  }

  def Success(parser: ResultParser, result: Any): ResultParser = {
    ResultParser(parser.sinConsumir.tail, result)
  }

  def char(caracter: Char, texto: String): ResultParser = char(caracter, ResultParser(texto, null))

  def char(caracter: Char, parser: ResultParser): ResultParser = try {
    def condicion(primerCaracter: Char, caracterBuscado: Char) = caracterBuscado == caracter

    parsearInput(condicion(parser.sinConsumir.head, caracter), parser, parser.sinConsumir.head)
  }
  catch {
    case error: Exception => Failure(parser, error)
  }

  def anyChar(texto: String): ResultParser = anyChar(ResultParser(texto, null))
  def anyChar(parser: ResultParser): ResultParser = try {
    def condicion(texto: String) = !texto.isBlank

    parsearInput(condicion(parser.sinConsumir), parser, parser.sinConsumir.head)
  }
  catch {
    case error: Exception => Failure(parser, error)
  }

  def parsearInput(condicion: Boolean, parser: ResultParser, resultado: Any): ResultParser = try {
    if (condicion) Success(parser, resultado)
    else throw new ParseoException(parser)
  } catch {
    case error: Exception => Failure(parser, error)
  }

}


//Para generar el AST usar Macros y QuasiCotes