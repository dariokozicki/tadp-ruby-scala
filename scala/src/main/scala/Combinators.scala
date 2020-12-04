import grupo3.ParsersTadp.{Parser, ParserException, Salida, char, double, integer, string}

import scala.util.Success


package object Combinators {
  implicit class ParserExtendido[T](parser1: Parser[T]) {
    def condicion(condicion: T => Boolean): Parser[T] = {
      val ret: Parser[T] = parser1(_).filter { case (resultado, _) => condicion(resultado) }
      ret.parseoException
    }

  def parseoException: Parser[T] = parser1(_).recover { case _ => throw new ParserException }

    def <|>[R >: T, G <: R](segundoParser: Parser[G]): Parser[R] = entrada => parser1(entrada).recoverWith{case _ => segundoParser(entrada)}

    def <>[G](segundoParser: Parser[G]): Parser[(T,G)] = entrada => for {
      (resultadoParser1, restoDelResultadoParser1) <- parser1(entrada)
      (resultadoParser2, restoDelResultadoParser2) <- segundoParser(restoDelResultadoParser1)
    }yield ((resultadoParser1, resultadoParser2), restoDelResultadoParser2)

    def ~>[T](segundoParser: Parser[T]):Parser[T] = entrada => for{
      ((_,resultadoParser2), restoDelResultadoParser2) <- (parser1 <> segundoParser) (entrada)
    } yield (resultadoParser2, restoDelResultadoParser2)

  def <~[R](segundoParser: Parser[R]): Parser[T] = entrada => for {
    ((resultadoParser1,_), restoParser2) <- (parser1 <> segundoParser) (entrada)
  } yield (resultadoParser1, restoParser2)

    //Hacer recursivo
    def sepBy[R](parserSeparador: Parser[R]): Parser[(T,T)] = (parser1 <~ parserSeparador) <> parser1

    def satisfies(condicion: T => Boolean): Parser[T] = {
      val retorno: Parser[T] = parser1(_).filter{ case (result,_) => condicion(result)}
      retorno.parseoException
    }

    def opt: Parser[Option[T]] = entrada => parser1.map(Some(_))(entrada).recover{ case _ => (None, entrada)}

    def map[R](funcionTransformacion: T => R): Parser[R] = entrada => for {
      (resultado, restoTransformacion) <- parser1(entrada)
    } yield (funcionTransformacion(resultado), restoTransformacion)

    def * : Parser[List[T]] = input => Success(kleene((List(), input)))
    private def kleene(accum: Salida[List[T]]): Salida[List[T]] = {
      parser1(accum._2).fold(
        _ => accum,
        { case (nuevoResultado, nuevoResto) => kleene((accum._1 :+ nuevoResultado, nuevoResto)) }
      )
    }

    // Tendria que ver como validar que se aplique una vez
    def + : Parser[List[T]] = parser1.*

  }
}