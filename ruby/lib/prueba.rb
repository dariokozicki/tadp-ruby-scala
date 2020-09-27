
module ContratoCondiciones
  attr_accessor :precondicion, :postcondicion, :invariants, :methods_redefined

  def attr_accessor(*args)
    @methods_redefined ||= [:initialize]
    @methods_redefined.push(*args)
    super(*args)
  end

  def evaluarInvariantes(contexto)
    if (!@invariants.all? { |bloque|  contexto.instance_eval(&bloque)})
      raise Exception.new "Failed to meet invariants"
    end
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
    @methods_redefined ||= [:initialize, :method_name]
    return if @methods_redefined.include?(method_name)
    newMethod = self.instance_method(method_name)
    @methods_redefined << method_name
    define_method(method_name) do
    |*arg|
      cumplePre = self.instance_eval(&self.class.precondicion)
      if (!cumplePre.nil?  && !cumplePre )
        raise Exception.new "Failed to meet precondition"
      end
      result = newMethod.bind(self).(*arg)
      cumplePost = self.instance_eval(&self.class.postcondicion)
      if(!cumplePost.nil? && !cumplePost)
        raise Exception.new "Failed to meet postcondition"
      end
      self.class.evaluarInvariantes(self)
      return result
    end
  end
end

Module.module_eval { prepend ContratoCondiciones }

#Todo lo de abajo son pruebas, deberian ir al test

class B
  attr_accessor :divisor

  invariant { divisor <= 4 }
  invariant { divisor != 0 }

  #post { empty? }
  def initialize(numero)
    @divisor = numero
  end

  pre{divisor != 0}
  post{divisor != 0}
  def saludo1 nombre1, nombre2
    return 4
  end

  pre{divisor != 0}
  post{divisor >= 4}
  def saludo2
    return (saludo1 "asd", "asd") == divisor
  end

  def saludo3 nombre
    puts 'chau' + " " + nombre
  end

  def cantidadPersonas cantidad, nombre
    puts 'chau' + " " + "#{4 + cantidad}" + " " + nombre
  end
end

prueba = B.new(4)
#puts prueba

puts prueba.saludo1 "Carlos","Alberto"
puts prueba.saludo2
prueba.saludo3 "Carlos"
prueba.cantidadPersonas 5 ,"Carlos"




#puts prueba.class.methods_redefined

#puts prueba.instance_variables
#puts prueba.class.invariants
#puts prueba.class.methods_redefined
#puts prueba.divisor
#prueba.initialize(5)
#prueba.cumpleInvariantes
#prueba.class.cumpleInvariantes()




