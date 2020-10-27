# frozen_string_literal: true

class Persona
  attr_accessor :energia

  def initialize(energia)
    puts 'initialize ' + energia.to_s
    @energia = energia
  end

  before_and_after_each_method(
    proc { puts 'before ' },
    proc { puts 'after' }
  )
  invariant { puts 'invariante de energia';energia >= 0}

  pre { puts 'el pre de hablar'; energia != 0 }
  post { puts 'el post de hablar'; energia != 0 }
  post { puts 'el otro post de hablar'}
  def hablar
    puts 'Hola'
    'Hola'
  end

  pre { puts 'el pre de correr'}
  post { puts 'esto no va a correr porque se rompe la invariante'}
  def correr
    puts 'correr'
    @energia = -1
  end
end
