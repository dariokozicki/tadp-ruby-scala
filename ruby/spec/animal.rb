
require_relative '../lib/contrato'
Contrato.activate

class Animal
  attr_accessor :energia

  invariant { energia > 0 }
  def comer gramos
    @energia += gramos * 5
  end

  pre { @energia > 0 }
  post { @energia <= 100 }
  def correr km
    @energia -= km * 2
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
    @energia = 80
    @desesperacion = 10
  end

  def hacer_sonido
    @energia = 13
    puts "GGGGRuau GGGGRRuau!"
  end
end

a = Dogo.new
a.correr 22
a.correr 22
a.correr 22
puts "Final"

=begin
describe 'prueba_test' do
  it 'main' do
    a = Animal.new
    a.energia
  end
end
=end
