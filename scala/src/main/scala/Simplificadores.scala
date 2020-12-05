import Simplificadores._
import grupo3.ParsersTadp.Figura

import scala.annotation.tailrec

package object Simplificadores {


  def isTransformacion(entrada: String): Boolean = {
    List("traslacion", "rotacion", "color", "escala").contains(entrada)
  }

  def simplificar(figura: Figura): Figura = {
    figura match {
      case ("color",(rgbs: List[Double], ("color", (rgbs2: List[Double], grupoColor2: Figura)))) =>
        if(rgbs.equals(rgbs2)) ("color", (rgbs, simplificar(grupoColor2))) else figura
      case ("rotacion", (List(0.0),fig: Figura)) => simplificar(fig)
      case ("escala", (List(1.0,1.0), fig: Figura)) => simplificar(fig)
      case ("traslacion", (List(0.0,0.0), fig:Figura)) => simplificar(fig)
      case ("grupo", figuras: List[(String, (List[Double], Figura))]) =>
        val name = figuras.head._1
        val values = figuras.head._2._1
        if (figuras.forall(fig => fig._1.equals(name)) && isTransformacion(name))
          (name, (values, ("grupo",figuras.map(fig => simplificar(fig._2._2))))) else ("grupo", figuras.map(simplificar))
      case ("rotacion", (ang1: List[Double], ("rotacion", (ang2: List[Double], fig: Figura)))) =>
        ("rotacion", (List(ang1.head + ang2.head), simplificar(fig)))
      case ("escala", (fact: List[Double], ("escala", (fact2: List[Double], fig: Figura)))) =>
        ("escala", (List(fact.head*fact2.head, fact.last*fact2.last), simplificar(fig)))
      case ("traslacion", (fact: List[Double], ("traslacion", (fact2:List[Double], fig: Figura)))) =>
        ("traslacion", (List(fact.head+fact2.head, fact.last+fact2.last), simplificar(fig)))
      case _ => figura
    }
  }

}
