# frozen_string_literal: true

class Persona
  include Contract
  attr_accessor :energia

  def initialize(energia)
    puts 'initialize ' + energia.to_s
    @energia = energia
  end

  before_and_after_each_method(
    proc { puts 'before ' },
    proc { puts 'after' }
  )
  invariant { energia >= 0 }

  def hablar
    puts 'Hola'
    'Hola'
  end

  def correr
    puts 'correr'
    @energia = -1
  end
end
