# frozen_string_literal: true

require_relative '../lib/conditions/condition'
require_relative '../lib/conditions/strategies/each_call'
require_relative '../lib/conditions/strategies/specific_call'
require_relative '../lib/conditions/strategies/method/method_data'
require_relative '../lib/contractor'

class Contract
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

  def guarantee(conditions_sym, instance, method_name, arg, result = nil)
    instance.class.ancestors.flat_map{ |anc| anc.contract.send(conditions_sym) }.each { |cond| cond.passes(instance, method_name, result, arg) }
  end

  def condition_method_data_set(method_data)
    methods_redefined << method_data.method_name
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

  def redefined?(method_name)
    methods_redefined.include? method_name
  end

end
