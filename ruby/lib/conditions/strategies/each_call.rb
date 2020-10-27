# frozen_string_literal: true

require_relative 'conditional_strategy'

class EachCall
  include ConditionalStrategy
  attr_accessor :active

  def initialize
    @active = true
  end

  def passes(instance, block, _method_name, result, *arg)
    return unless active

    begin
      self.active = !active
      super
    ensure
      self.active = !active
    end
  end

  def method_data=(_method_data)
    nil # do nothing
  end

  def method_data
    nil
  end
end
