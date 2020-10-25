# frozen_string_literal: true

class Condition
  attr_accessor :conditional_block
  attr_accessor :conditional_strategy

  def initialize(conditional_block, conditional_strategy)
    @conditional_strategy = conditional_strategy
    @conditional_block = conditional_block
  end

  def passes(instance, method_name, *arg)
    @conditional_strategy.passes(instance, @conditional_block, method_name, *arg)
  end

  def method_name=(method_name)
    conditional_strategy.method_name = method_name
  end

  def method_name
    conditional_strategy.method_name
  end
end
