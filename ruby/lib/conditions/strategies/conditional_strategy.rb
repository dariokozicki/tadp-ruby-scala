require_relative '../../exceptions/contract_exception'
module ConditionalStrategy
  attr_accessor :active

  def passes(instance, block, _method_name, result, *arg)
    self.active = !active
    res = instance.instance_exec(result, *arg, &block)
    raise ContractException if res == false
  ensure
    self.active = !active
  end

  def method_data=(_method_data)
    raise 'Not implemented'
  end

  def method_data
    raise 'Not implemented'
  end
end