
//trait Combinators extends ((String) => ResultParser => ResultParser => Try[ResultParser])

package object ParsersOld {
  /*
    //type F = List[Any] => List[Any]

    case class ResultParser(sinConsumir: String, val resultado: Any) {


      /*def <|>(parser1:ResultParser):ResultParser = try {
         print("prueba")
         true || false
         parser1
       } catch {
         case error: Exception => Failure(parser1, error)
       }*/
    }

    case class ParseoException(val parser: ResultParser) extends RuntimeException

    //class modificadores(primero: Parser, segundo: Parser) extends Parser //para modificadores podria utilizar Implicit Class

    def Failure(parser: ResultParser, error: Exception): ResultParser = {
      //ResultParser(parser.sinConsumir, null)
      throw ParseoException(parser)
    }

    def Success(parser: ResultParser, result: Any): ResultParser = {
      ResultParser(parser.sinConsumir.tail, result)
    }

    def char(caracter: Char, texto: String): ResultParser = char(caracter, ResultParser(texto, null))

    def char(caracter: Char, parser: ResultParser): ResultParser = try {
      def condicion(primerCaracter: Char, caracterBuscado: Char) = caracterBuscado == primerCaracter

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

    def digit(texto: String): ResultParser = digit(ResultParser(texto, null))
    def digit(parser: ResultParser): ResultParser = try {

      def condicion(caracter: Char) = caracter.isDigit

      parsearInput(condicion(parser.sinConsumir.head), parser, parser.sinConsumir.head.asDigit)

    }
    catch {
      case error: Exception => Failure(parser, error)
    }

    def string(cadenaABuscar: String, texto: String): ResultParser = string(cadenaABuscar, ResultParser(texto, null))
    def string(cadenaABuscar: String, parser:ResultParser):ResultParser = try{
      def condicion(cadena: String, texto: String) = texto.contains(cadena)

      val index = parser.sinConsumir.indexOf(cadenaABuscar)
      parsearInput(condicion(cadenaABuscar,parser.sinConsumir), parser, parser.sinConsumir.substring(index,cadenaABuscar.length) )

    }catch {
      case error: Exception => Failure(parser, error)
    }

    def integer(texto: String):ResultParser = integer(texto, ResultParser(texto,null))
    def integer(texto: String, parser: ResultParser): ResultParser = try{

      def condicion(texto: String) = texto.toInt.isValidInt
      parsearInput(condicion(texto),parser,texto.toInt)

    }catch {
      case error: Exception => Failure(parser, error)
    }

    // TODO: Revisar enunciado e implementacion
    def double(numero: Int):ResultParser = double(numero, ResultParser(numero.toString,null))
    def double(numero: Int, parser: ResultParser): ResultParser = try{
      parser.copy(resultado = numero.toDouble)
    }catch {
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
  /* Otra implementacion con PM */
  trait parsers
  case object char extends parsers
  case object anyChar extends parsers
  case object string extends parsers
  case object integer extends parsers
  case object double extends parsers

  package object CombinatorsOld{

    case class ResultCombinator(resultCombinator1: ResultParser, resultCombinator2: ResultParser) {
      def <|>(aux: ResultParser): ResultParser = {
        aux
      }

    }*/
}