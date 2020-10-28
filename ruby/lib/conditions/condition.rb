# frozen_string_literal: true
class Condition
  attr_accessor :conditional_block, :conditional_strategy

  def initialize(conditional_block, conditional_strategy)
    @conditional_strategy = conditional_strategy
    @conditional_block = conditional_block
  end

  def passes(instance, method_name, result, arg)
    @conditional_strategy.passes(instance, @conditional_block, method_name, result, arg)
  end

  def method_data=(method_data)
    conditional_strategy.method_data = method_data
  end

  def method_data
    conditional_strategy.method_data
  end
end
