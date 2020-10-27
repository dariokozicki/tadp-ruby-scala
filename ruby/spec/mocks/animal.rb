class Animal
  attr_accessor :energia
  invariant { energia > 0 }

  def initialize
    @energia = 100
    @inmovil = false
  end

  pre { energia > 10 }
  post { energia <= 100 }
  def comer gramos
    @energia += gramos * 5
  end

  post { energia <= 100 }
  def correr km
    @energia -= km * 2
    @inmovil = true
  end

end

class Gato < Animal
  def initialize
    @energia = 60
    @pulcritud = 10
  end

  def limpiarse
    @energia -= 2
    @pulcritud += 10
  end

  def generar_sonido
    puts "Miaaau!"
  end

end

class Perro < Animal
  attr_accessor :desesperacion
  invariant { desesperacion < 100 }

  def initialize
    @energia = 100
    @desesperacion = 10
  end

  def generar_sonido
    puts "Guau guau!"
  end

  def ladrar_rueda
    @desesperacion += 12
  end
end

class Dogo < Perro

  def initialize
    @energia = 100
    @desesperacion = 10
  end

  def hacer_sonido
    puts "GGGGRuau GGGGRRuau!"
  end
end