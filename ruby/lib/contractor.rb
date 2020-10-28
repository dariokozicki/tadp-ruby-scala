# frozen_string_literal: true

require_relative '../lib/contract'

module Contractor
  attr_writer :contract

  def contract
    @contract ||= Contract.new
  end

  def method_added(method_name)
    return if contract.redefined? method_name

    old_method = instance_method(method_name)
    condition_method_data_set(method_name)
    define_method(method_name) do |*arg, &block|
      self.class.contract.guarantee(:preconditions, self, method_name, arg)
      result = old_method.bind(self).call(*arg, &block)
      self.class.contract.guarantee(:postconditions, self, method_name, arg, result)
      result
    end
  end

  def condition_method_data_set(method_name)
    method_data = MethodData.new(method_name, instance_method(method_name).parameters.map{ |params| params[1]})
    contract.condition_method_data_set(method_data)
  end

  def before_and_after_each_method(precondition_block, postcondition_block)
    contract.before_and_after_each_method(precondition_block, postcondition_block)
  end

  def pre(&before_block)
    contract.pre(&before_block)
  end

  def post(&after_block)
    contract.post(&after_block)
  end

  def invariant(&block)
    contract.invariant(&block)
  end

  def self.activate
    Module.prepend(Contractor)
  end
end

