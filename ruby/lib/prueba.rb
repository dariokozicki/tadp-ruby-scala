
module ContratoCondiciones
  attr_accessor :before, :after, :precondicion, :postcondicion, :invariants, :methods_aliased

  def before_and_after_each_method(before, after)
    @before = before
    @after = after
  end

  def cumpleInvariantes
    @invariants.all? { |bloque|  self.instance_eval(&bloque)}
  end

  def invariant(&bloqueInvariant)
    @invariants ||= []
    if !@invariants.include?(bloqueInvariant)
      @invariants << bloqueInvariant
    end
  end

  def pre(&antesBloque)
    @precondicion ||= Proc.new {}
    @precondicion = antesBloque
  end

  def post(&despuesBloque)
    @postcondicion ||=  Proc.new {}
    @postcondicion = despuesBloque
  end

  def method_added(method_name)
    @methods_aliased ||= []
    return if method_name == :method_added || method_name.to_s['original'] || @methods_aliased.include?(method_name)
    original_method = 'original_' + method_name.to_s
    alias_method original_method, method_name
    @methods_aliased << method_name
    define_method(method_name) do
    |*arg|
      puts self.class.precondicion
      self.instance_eval(&self.class.before)
      send(original_method,*arg)
      self.instance_eval(&self.class.after)
    end
  end

end

Module.module_eval { prepend ContratoCondiciones }

class B
  attr_accessor :divisor

  invariant { divisor >= 0 }
  invariant { divisor != 0 }

  #post { empty? }
  def initialize(numero)
    @divisor = numero
  end


  pre{divisor != 0}
  post{divisor != 0}
  def saludo1 nombre1, nombre2
    puts 'chau2' + " "+  nombre1 + " " + nombre2
  end

  def saludo2
    puts 'chau2'
  end

  before_and_after_each_method(proc {puts 'antes'},proc{puts 'despues'} )

  def saludo3 nombre
    puts 'chau' + " " + nombre
  end

  def cantidadPersonas cantidad, nombre
    puts 'chau' + " " + "#{4 + cantidad}" + " " + nombre
  end
end

prueba = B.new(4)

prueba.saludo1 "Carlos","Alberto"
prueba.saludo2
prueba.saludo3 "Carlos"
prueba.cantidadPersonas 5 ,"Carlos"
puts prueba.class.methods_aliased

puts prueba.class.invariants
#prueba.class.cumpleInvariantes




