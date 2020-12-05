import grupo3.ParsersTadp.{Parser, ParserException, Salida, char, double, integer, string}

import scala.util.{Failure, Success, Try}


package object Combinators {
  implicit class ParserExtendido[T](parser1: Parser[T]) {
    def satisfies(condicion: T => Boolean): Parser[T] = {
      val ret: Parser[T] = parser1(_).filter { case (resultado, _) => condicion(resultado) }
      ret.parseoException
    }

    def parseoException: Parser[T] = parser1(_).recover { case _ => throw new ParserException }

    def <|>[R >: T, G <: R](segundoParser: Parser[G]): Parser[R] = entrada => {
      try {
        parser1(entrada).recoverWith { case _ => segundoParser(entrada) }
      } catch{
        case ex: ParserException => segundoParser(entrada)
      }
    }

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
    def sepBy[R](parserSeparador: Parser[R]): Parser[List[T]] = (parser1 <~ (parserSeparador.opt)).+

    def opt: Parser[Option[T]] = entrada => {
      try {
        parser1(entrada).map(tuple => (Some(tuple._1), tuple._2)).fold(ex => Try(None, entrada), res => Try(res))
      } catch{
        case ex: ParserException => Try(None, entrada)
      }
    }

    def map[R](funcionTransformacion: T => R): Parser[R] = entrada => for {
      (resultado, restoTransformacion) <- parser1(entrada)
    } yield (funcionTransformacion(resultado), restoTransformacion)

    def * : Parser[List[T]] = input => Success(kleene((List(), input)))

    private def kleene(accum: Salida[List[T]]): Salida[List[T]] = {
      try {
        parser1(accum._2).fold(
          _ => accum,
          { case (nuevoResultado, nuevoResto) => kleene((accum._1 :+ nuevoResultado, nuevoResto)) }
        )
      }catch{
        case _ : ParserException => accum
      }
    }

    def + : Parser[List[T]] = parser1.*.satisfies(_.nonEmpty)

  }
}