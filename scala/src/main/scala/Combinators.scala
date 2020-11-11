import grupo3.ParsersTadp.{Parser, ParserException}


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
  }
}