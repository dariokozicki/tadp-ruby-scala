package object Figuras {
  abstract class Figura{}
  case class Punto(x: Double, y: Double)
  case class Circulo(punto: Punto, radio: Double) extends Figura
  case class Triangulo(x: Punto, y: Punto, z: Punto) extends Figura
  case class Rectangulo(x: Punto, y: Punto) extends Figura
  case class Grupo(figuras: List[Figura]) extends Figura
  case class Rotacion(angulo: Double, figura: Figura) extends Figura
  case class Traslacion(punto: Punto, figura: Figura) extends Figura
  case class Escala(punto: Punto, figura: Figura) extends Figura
  case class Color(r: Double, g: Double, b: Double, figura: Figura) extends Figura
}
