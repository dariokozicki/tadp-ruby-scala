# frozen_string_literal: true

require_relative 'conditional_strategy'

class EachCall
  include ConditionalStrategy

  def initialize
    @active = true
  end


  def passes(instance, block, _method_name, result, *arg)
    return unless @active

    super
  end

  def method_data=(_method_data)
    nil # do nothing
  end

  def method_data
    nil
  end
end
