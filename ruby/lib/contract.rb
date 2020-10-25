# frozen_string_literal: true

Dir['./lib/*'].sort.each { |file| require file }
module Contract
  attr_writer :preconditions, :postconditions, :methods_redefined
  def preconditions
    @preconditions ||= []
  end

  def postconditions
    @postconditions ||= []
  end

  def methods_redefined
    @methods_redefined ||= []
  end

  def method_added(method_name)
    return if methods_redefined.include? method_name

    methods_redefined << method_name
    old_method = instance_method(method_name)
    (postconditions + preconditions)
      .each do |cond|
      cond.method_name = method_name if cond.method_name.nil?
    end

    define_method(method_name) do |*arg, &block|
      self.class.guarantee(self.class.preconditions, self, method_name, *arg)

      result = old_method.bind(self).call(*arg, &block)
      self.class.guarantee(self.class.postconditions, self, method_name, *arg)

      result
    end
  end

  def guarantee(conditions, instance, method_name, *arg)
    conditions.all? { |precond| precond.passes(instance, method_name, *arg) }
  end

  def before_and_after_each_method(precondition_block, postcondition_block)
    preconditions << Precondition.new(precondition_block, EachCall.new)
    postconditions << Postcondition.new(postcondition_block, EachCall.new)
  end

  def pre(&before_block)
    preconditions << Precondition.new(before_block, SpecificCall.new)
  end

  def post(&after_block)
    postconditions << Postcondition.new(after_block, SpecificCall.new)
  end

  def invariant(&block)
    postconditions << Postcondition.new(block, EachCall.new)
  end

  def attr_accessor(*args) # es necesario para evitar recursividad
    methods_redefined.push(*args)
    super(*args)
  end

  def attr_reader(*args)
    methods_redefined.push(*args)
    super(*args)
  end

  def attr_writer(*args)
    methods_redefined.push(*args)
    super(*args)
  end

  def self.activate
    Module.prepend(Contract)
  end

end
