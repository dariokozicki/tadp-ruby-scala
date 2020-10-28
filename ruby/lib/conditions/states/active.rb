class Active
  attr_accessor :condition
  def initialize(condition)
    @condition = condition
  end
  
  def passes(conditional_strategy, instance, conditional_block, method_name, result, arg)
    conditional_strategy.passes(instance, conditional_block, method_name, result, arg)
  end
end
