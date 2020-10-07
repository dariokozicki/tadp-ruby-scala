class Operaciones
  attr_accessor :variable1, :variable2
  def initialize(valor1,valor2)
    @variable1 = valor1
    @variable2 = valor2
  end

  #precondición de dividir
  pre { divisor != 0 }
  #postcondición de dividir
  post { |result| result * divisor == dividendo }
  def dividir(dividendo, divisor)
    dividendo / divisor
  end
end