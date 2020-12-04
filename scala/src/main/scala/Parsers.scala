package grupo3
import Combinators._

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

  def iterarString(entrada: Entrada, string: String): Try[(String, Entrada)] = {
    try{
      if(string.startsWith(entrada.substring(0,string.length))) {
        Try(string, entrada.substring(string.length, entrada.length))
      }else{
        throw new ParserException()
      }
    }catch {
      case error: Exception => throw ParserException()
    }
  }

  val string: String => Parser[String] = string => iterarString(_,string)

  private def stringParseo[T](valor: T): Parser[T] = entrada => Success((valor, entrada))
  val stringAux: String => Parser[String] = _.toList.map(char(_)).foldLeft(stringParseo("")) {
    (parserAcumulador: Parser[String], parserChar) => (parserAcumulador <> parserChar).map{
      case (stringAcumulado, nuevoChar) => stringAcumulado + nuevoChar.toString
    }
  }
 /* val integer: Parser[Int] = {
    val ret: Parser[Int] = input => Try(input.toInt, "")
    ret.parseoException
  }*/
 val integer: Parser[Int] = try{
   val ret: Parser[Int] = digit.+.map(_.mkString.toInt)
   ret.parseoException
 }
  val double: Parser[Double] = {
    val ret: Parser[Double] = input => Try(input.toDouble, "")
    ret.parseoException
  }


  /*def iterarInt(entrada: Entrada, acum: String = ""): Try[(Int,Entrada)] = {
    try{
      if((acum.isEmpty && entrada.head == '-') || entrada.head.isDigit){
        iterarInt(entrada.tail, acum + entrada.head);
      }
      else if(entrada.isEmpty && !acum.isEmpty && acum != "-"){
        Try(acum.toInt, entrada)
      }
      else{
        Try(acum.toInt, entrada)
        throw new ParserException()
      }
    }catch {
      case error: Exception => throw ParserException()
    }
  }*/

  private def intParseo[T](valor: T): Parser[T] = entrada => Success((valor, entrada))
  val intAux: String => Parser[String] = _.toList.map(char(_)).foldLeft(stringParseo("")) {
    (parserAcumulador: Parser[String], parserChar) => (parserAcumulador <> parserChar).map{
      case (stringAcumulado, nuevoChar) => stringAcumulado + nuevoChar.toString
    }
  }




}