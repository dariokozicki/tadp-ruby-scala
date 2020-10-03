
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

=begin
a = Dogo.new
a.correr 22
a.correr 22
a.correr 22
puts "Final"
=end

describe 'PRUEBAS EN CLASES' do
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

describe 'PRUEBAS EN SUBCLASES' do
  let(:una_sub){
    Perro.new
  }

  it 'Subclase cumple invariant de super' do
    una_sub.correr 12
    una_sub.correr 15
    expect(una_sub.energia).to eq(46)
  end

  it 'Subclase NO cumple invariant de super' do
    una_sub.correr 22
    una_sub.correr 15
    expect{ una_sub.correr 18 }.to raise_error("Failed to meet invariants")
  end

  it 'Subclase cumple precondicion de super' do
    una_sub.correr 15
    una_sub.correr 25
    una_sub.comer 5
    expect(una_sub.energia).to eq(45)
  end

  it 'Subclase NO cumple precondicion de super' do
    una_sub.correr 21
    una_sub.correr 25
    expect{ una_sub.comer 5 }.to raise_error("Failed to meet precondition")
  end

  it 'Subclase cumple postcondicion de super' do
    una_sub.correr 15
    una_sub.comer 3
    expect(una_sub.energia).to eq(85)
  end

  it 'Subclase NO cumple postcondicion de super' do
    una_sub.correr 15
    expect{ una_sub.comer 7 }.to raise_error("Failed to meet postcondition")
  end
end

describe 'PRUEBAS EN SUBCLASE DE SUBCLASE' do
  let(:una_sub_sub){
    Dogo.new
  }

  it 'Sub-subclase cumple invariant de super' do
    una_sub_sub.correr 12
    una_sub_sub.correr 15
    expect(una_sub_sub.energia).to eq(46)
  end

  it 'Sub-subclase NO cumple invariant de super' do
    una_sub_sub.correr 22
    una_sub_sub.correr 15
    expect{ una_sub_sub.correr 18 }.to raise_error("Failed to meet invariants")
  end

  it 'Sub-subclase cumple precondicion de super' do
    una_sub_sub.correr 15
    una_sub_sub.correr 25
    una_sub_sub.comer 5
    expect(una_sub_sub.energia).to eq(45)
  end

  it 'Sub-subclase NO cumple precondicion de super' do
    una_sub_sub.correr 21
    una_sub_sub.correr 25
    expect{ una_sub_sub.comer 5 }.to raise_error("Failed to meet precondition")
  end

  it 'Sub-subclase cumple postcondicion de super' do
    una_sub_sub.correr 15
    una_sub_sub.comer 3
    expect(una_sub_sub.energia).to eq(85)
  end

  it 'Sub-subclase NO cumple postcondicion de super' do
    una_sub_sub.correr 15
    expect{ una_sub_sub.comer 7 }.to raise_error("Failed to meet postcondition")
  end
end