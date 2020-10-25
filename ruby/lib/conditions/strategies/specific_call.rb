# frozen_string_literal: true

require_relative 'conditional_strategy'

class SpecificCall
  include ConditionalStrategy
  attr_accessor :method_name

  def passes(instance, block, method_name, *arg)
    return true unless method_name == @method_name

    res = instance.instance_exec(&block)
    raiseOnFalse res
  end

  def method_name=(method_name)
    @method_name = method_name
  end

  def method_name
    @method_name
  end
end
