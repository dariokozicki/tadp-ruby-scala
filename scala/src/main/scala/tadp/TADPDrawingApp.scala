package tadp

import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter

object TADPDrawingApp extends App {
// Ejemplo de uso del drawing adapter:

  case class Puntos(x: Double, y: Double)

  TADPDrawingAdapter
    .forScreen { adapter =>
      adapter
        .beginColor(Color.rgb(100, 100, 100))
        .rectangle((200, 200), (400, 400))
        .end()
    }

  val triangulo: List[String] => Unit = {
    _ match {
      case List(a,b,c) => TADPDrawingAdapter.forScreen{ adapter =>
        adapter.triangle((Puntos(2,2)),
          (b.split("@").head.toDouble,b.split("@")[1].toString().toDouble),
          (c.split("@").head.toDouble,c.split("@")[1].toString().toDouble))
      }
    }
  }
}
