
module ContratoCondiciones
  attr_accessor :before, :after, :precondicion, :postcondicion, :invariants, :methods_redefined

  def before_and_after_each_method(before, after)
    @before = before
    @after = after
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
    @methods_redefined ||= []
    return if method_name == :method_added  || @methods_redefined.include?(method_name)
    newMethod = self.instance_method(method_name)
    @methods_redefined << method_name
    puts method_name
    define_method(method_name) do
    |*arg|
      #puts self.class.invariants
      #self.class.cumpleInvariantes(self)
      #puts self.class.precondicion
      cumplePre = self.instance_eval(&self.class.before)
      if (!cumplePre.nil?  && !cumplePre )
        raise Exception.new "Failed to meet precondition"
      end
      newMethod.bind(self).(*arg)
      cumplePost = self.instance_eval(&self.class.after)
      if(!cumplePost.nil? && !cumplePost)
        raise Exception.new "Failed to meet postcondition"
      end
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
puts prueba

prueba.saludo1 "Carlos","Alberto"
prueba.saludo2
prueba.saludo3 "Carlos"
prueba.cantidadPersonas 5 ,"Carlos"
#puts prueba.class.methods_redefined

puts prueba.instance_variables
puts prueba.class.invariants
puts prueba
#prueba.cumpleInvariantes
#prueba.class.cumpleInvariantes()




