
require_relative '../lib/contrato'
Contrato.activate
require 'rspec'

class Animal
  attr_accessor :energia
  invariant { energia > 0 }

  def initialize
    @energia = 100
    @inmovil = false
  end

  pre { @energia > 10 }
  post { @energia <= 100 }
  def comer gramos
    @energia += gramos * 5
  end

  post { @energia <= 100 }
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
    @energia = 80
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
    @energia = 60
    @desesperacion = 10
  end

  def hacer_sonido
    puts "GGGGRuau GGGGRRuau!"
  end
end

=begin
a = Dogo.new
a.correr 22
a.correr 22
a.correr 22
puts "Final"
=end

describe 'Pruebas de clase' do
  let(:una_clase){
    Animal.new
  }

  it 'Clase cumple invariant' do
    una_clase.correr 12
    una_clase.correr 15
    expect(una_clase.energia).to eq(46)
  end

  it 'Clase NO cumple invariant' do
    una_clase.correr 22
    una_clase.correr 15
    expect{ una_clase.correr 18 }.to raise_error("Failed to meet invariants")
  end

  it 'Clase cumple precondicion' do
    una_clase.correr 15
    una_clase.correr 25
    una_clase.comer 5
    expect(una_clase.energia).to eq(45)
  end

  it 'Clase NO cumple precondicion' do
    una_clase.correr 21
    una_clase.correr 25
    expect{ una_clase.comer 5 }.to raise_error("Failed to meet precondition")
  end

  it 'Clase cumple postcondicion' do
    una_clase.correr 15
    una_clase.comer 3
    expect(una_clase.energia).to eq(85)
  end

  it 'Clase NO cumple postcondicion' do
    una_clase.correr 15
    expect{ una_clase.comer 7 }.to raise_error("Failed to meet postcondition")
  end

end
