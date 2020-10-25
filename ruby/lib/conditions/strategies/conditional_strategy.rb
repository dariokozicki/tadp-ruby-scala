require_relative '../../exceptions/contract_exception'
module ConditionalStrategy
  def passes(instance, conditional_block, method_name, *arg)
    raise 'Not implemented'
  end

  def raiseOnFalse(result)
    raise ContractException if result == false # no uso !result porque podria no ser un booleano

    true
  end
end