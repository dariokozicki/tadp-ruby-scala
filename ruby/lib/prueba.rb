class Module
  attr_accessor :before, :after
  def before_and_after_each_method(before, after)
    @before = before
    @after = after
  end

  def method_added(method_name)
    @methods_aliased ||= []
    return if method_name == :method_added || method_name.to_s['original'] || @methods_aliased.include?(method_name)

    original_method = 'original_' + method_name.to_s
    alias_method original_method, method_name
    @methods_aliased << method_name
    define_method method_name do
      self.class.before.call
      send(original_method)
      self.class.after.call
    end
  end

end




class B

  def saludo1
    puts 'chau2'
  end

  before_and_after_each_method(proc {puts 'antes'}, proc {puts 'despues'})

  def saludo3
    puts 'chau'
  end
end

B.new.saludo1
B.new.saludo3