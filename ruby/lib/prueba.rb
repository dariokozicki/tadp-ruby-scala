
module ContratoCondiciones
  attr_accessor :before, :after
  def before_and_after_each_method(before, after)
    @before = before
    @after = after
  end

  def pre(&pre)
    @before = pre
  end

  def post(&post)
    @after = post
  end

  def method_added(method_name)
    @methods_aliased ||= []
    return if method_name == :method_added || method_name.to_s['original'] || @methods_aliased.include?(method_name)

    original_method = 'original_' + method_name.to_s
    alias_method original_method, method_name
    @methods_aliased << method_name
    define_method(method_name) do
    |*arg|
      self.class.before.call
      send(original_method,*arg)
      self.class.after.call
    end
  end

end

Module.module_eval { prepend ContratoCondiciones }

class B

  attr_accessor :divisor
  #invariant { capacity >= 0 }
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

  def cantidadPersonas cantidad
    puts 4 + cantidad
  end
end

B.new.saludo1 "Carlos","Alberto"
B.new.saludo2
B.new.saludo3 "Carlos"
B.new.cantidadPersonas 5




