require_relative 'conditional_strategy'

class EachCall
  include ConditionalStrategy
  def passes(instance, block, _method_name, result, *arg)
    res = instance.instance_exec(result, *arg, &block)
    super
  end

  def method_data=(_method_data)
    nil #do nothing
  end

  def method_data
    nil
  end
end
