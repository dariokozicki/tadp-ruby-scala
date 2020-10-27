require_relative '../../exceptions/contract_exception'
module ConditionalStrategy
  def passes(instance, block, _method_name, result, *arg)
    res = instance.instance_exec(result, *arg, &block)
    raise ContractException if res == false
  end

  def method_data=(_method_data)
    raise 'Not implemented'
  end

  def method_data
    raise 'Not implemented'
  end
end