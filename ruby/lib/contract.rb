# frozen_string_literal: true

require_relative '../lib/conditions/condition'
require_relative '../lib/conditions/strategies/each_call'
require_relative '../lib/conditions/strategies/specific_call'
require_relative '../lib/conditions/strategies/method/method_data'

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

    old_method = instance_method(method_name)
    condition_method_data_set(method_name)
    define_method(method_name) do |*arg, &block|
      self.class.guarantee(:preconditions, self, method_name, arg)

      result = old_method.bind(self).call(*arg, &block)
      self.class.guarantee(:postconditions, self, method_name, arg, result)

      result
    end
  end

  def guarantee(conditions_sym, instance, method_name, arg, result = nil)
    ancestors.flat_map(&conditions_sym).each { |cond| cond.passes(instance, method_name, result, arg) }
  end

  def condition_method_data_set(method_name)
    methods_redefined << method_name
    method_data = MethodData.new(method_name, instance_method(method_name).parameters.map{ |params| params[1]})
    (postconditions + preconditions)
      .each do |cond|
      cond.method_data = method_data if cond.method_data.nil?
    end
  end

  def before_and_after_each_method(precondition_block, postcondition_block)
    preconditions << Condition.new(precondition_block, EachCall.new)
    postconditions << Condition.new(postcondition_block, EachCall.new)
  end

  def pre(&before_block)
    preconditions << Condition.new(before_block, SpecificCall.new)
  end

  def post(&after_block)
    postconditions << Condition.new(after_block, SpecificCall.new)
  end

  def invariant(&block)
    postconditions << Condition.new(block, EachCall.new)
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
