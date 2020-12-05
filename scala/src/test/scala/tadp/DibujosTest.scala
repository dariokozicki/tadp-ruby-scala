package tadp

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import tadp.TADPDrawingApp.dibujarScreen

class DibujosTest extends AnyFlatSpec with should.Matchers {

  it should "dibujar grupo" in {
    val grupo = "grupo(circulo[200 @ 350, 100], triangulo[200   @  50, 101 @ 335, 299 @ 335], rectangulo[10 @ 20, 30 @ 40])"
    dibujarScreen(grupo)
  }

  it should "dibujar triangulo" in {
    val triangulo = "triangulo[200   @  50, 101 @ 335, 299 @ 335]"
    dibujarScreen(triangulo)
  }

  it should "dibujar corazon" in {
    val corazon = "color[200, 0, 0](grupo(escala[1, 0.8](grupo(circulo[210 @ 250, 100],circulo[390 @ 250, 100])),rectangulo[200 @ 170, 400 @ 300],triangulo[113 @ 225, 487 @ 225, 300 @ 450]))"
    dibujarScreen(corazon)
  }

  it should "dibujar cuadrados con colores" in {
    val cuadrados = "escala[1.45, 1.45](grupo(color[0, 0, 0](rectangulo[0 @ 0, 400 @ 400]),color[200, 70, 0](rectangulo[0 @ 0, 180 @ 150]),color[250, 250, 250](grupo(rectangulo[186 @ 0, 400 @ 150],rectangulo[186 @ 159, 400 @ 240],rectangulo[0 @ 159, 180 @ 240],rectangulo[45 @ 248, 180 @ 400],rectangulo[310 @ 248, 400 @ 400],rectangulo[186 @ 385, 305 @ 400])),color[30, 50, 130](rectangulo[186 @ 248, 305 @ 380]),color[250, 230, 0](rectangulo[0 @ 248, 40 @ 400])))"
    dibujarScreen(cuadrados)
  }

  it should "dibujar murcielago" in {
    val murcielago = "grupo(escala[1.2, 1.2](grupo(color[0, 0, 80](rectangulo[0 @ 0, 600 @ 700]),color[255, 255, 120](circulo[80 @ 80, 50]),color[0, 0, 80](circulo[95 @ 80, 40]))),color[50, 50, 50](triangulo[80 @ 270, 520 @ 270, 300 @ 690]),color[80, 80, 80](triangulo[80 @ 270, 170 @ 270, 300 @ 690]),color[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 150]),color[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 400]),color[150, 150, 150](triangulo[400 @ 200, 300 @ 400, 420 @ 320]),color[150, 150, 150](triangulo[300 @ 400, 200 @ 200, 180 @ 320]),color[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 180 @ 320]),color[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 150 @ 120]),color[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 420 @ 320]),color[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 450 @ 120]),grupo(escala[0.4, 1](color[0, 0,0](grupo(circulo[970 @ 270, 25],circulo[530 @ 270, 25])))))"
    dibujarScreen(murcielago)
  }

  it should "dibujar a pepita" in {
    val pepita = "grupo(color[0, 0, 80](grupo(triangulo[50 @ 400, 250 @ 400, 200 @ 420],triangulo[50 @ 440, 250 @ 440, 200 @ 420])),color[150, 150, 150](triangulo[200 @ 420, 250 @ 400, 250 @ 440]),color[180, 180, 160](triangulo[330 @ 460, 250 @ 400, 250 @ 440]),color[200, 200, 180](grupo(triangulo[330 @ 460, 400 @ 400, 330 @ 370],triangulo[330 @ 460, 400 @ 400, 370 @ 450],triangulo[400 @ 430, 400 @ 400, 370 @ 450],triangulo[330 @ 460, 250 @ 400, 330 @ 370])),grupo(color[150, 0, 0](grupo(triangulo[430 @ 420, 400 @ 400, 450 @ 400],triangulo[430 @ 420, 400 @ 400, 400 @ 430])),color[100, 0, 0](triangulo[420 @ 420, 420 @ 400, 400 @ 430]),color[0, 0, 60](grupo(triangulo[420 @ 400, 400 @ 400, 400 @ 430],triangulo[420 @ 380, 400 @ 400, 450 @ 400],triangulo[420 @ 380, 400 @ 400, 300 @ 350])),color[150, 150, 0](triangulo[440 @ 410, 440 @ 400, 460 @ 400])),color[0, 0, 60](grupo(triangulo[330 @ 300, 250 @ 400, 330 @ 370],triangulo[330 @ 300, 400 @ 400, 330 @ 370],triangulo[360 @ 280, 400 @ 400, 330 @ 370],triangulo[270 @ 240, 100 @ 220, 330 @ 370],triangulo[270 @ 240, 360 @ 280, 330 @ 370])))"
    dibujarScreen(pepita)
  }

  it should "dibujar a red" in {
    val red = "color[100, 100, 100](grupo(color[0, 0, 0](grupo(color[201, 176, 55](triangulo[0 @ 0, 650 @ 0, 0 @ 750]),color[215, 215, 215](triangulo[650 @ 750, 650 @ 0, 0 @ 750]),color[255, 255, 255](grupo(rectangulo[230 @ 150, 350 @ 180],rectangulo[110 @ 150, 470 @ 390])),color[255, 0, 0](grupo(rectangulo[170 @ 60, 410 @ 150],rectangulo[350 @ 60, 380 @ 180],rectangulo[200 @ 60, 230 @ 180],rectangulo[260 @ 300, 320 @ 330],rectangulo[170 @ 390, 410 @ 480])),rectangulo[200 @ 180, 380 @ 210],rectangulo[230 @ 240, 260 @ 300],rectangulo[320 @ 240, 350 @ 300],rectangulo[200 @ 30, 380 @ 60],rectangulo[170 @ 60, 200 @ 90],rectangulo[380 @ 60, 410 @ 90],rectangulo[140 @ 90, 170 @ 150],rectangulo[410 @ 90, 440 @ 150],rectangulo[110 @ 150, 200 @ 180],rectangulo[110 @ 180, 170 @ 210],rectangulo[140 @ 210, 170 @ 240],rectangulo[80 @ 210, 110 @ 270],rectangulo[110 @ 270, 170 @ 330],rectangulo[110 @ 300, 200 @ 330],rectangulo[80 @ 330, 110 @ 390],rectangulo[110 @ 390, 200 @ 420],rectangulo[140 @ 420, 170 @ 480],rectangulo[200 @ 420, 260 @ 450],rectangulo[320 @ 420, 380 @ 450],rectangulo[260 @ 390, 320 @ 420],rectangulo[170 @ 330, 410 @ 390],rectangulo[170 @ 480, 260 @ 510],rectangulo[260 @ 450, 320 @ 480],rectangulo[320 @ 480, 410 @ 510],rectangulo[410 @ 420, 440 @ 480],rectangulo[380 @ 390, 470 @ 420],rectangulo[470 @ 330, 500 @ 390],rectangulo[380 @ 300, 470 @ 330],rectangulo[410 @ 270, 470 @ 330],rectangulo[470 @ 210, 500 @ 270],rectangulo[410 @ 210, 440 @ 240],rectangulo[410 @ 180, 470 @ 210],rectangulo[380 @ 150, 470 @ 180],rectangulo[380 @ 150, 470 @ 180]))))"
    dibujarScreen(red)
  }

}
