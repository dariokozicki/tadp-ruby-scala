# frozen_string_literal: true

require_relative 'conditional_strategy'
require_relative '../condition_runner'

class SpecificCall
  include ConditionalStrategy
  attr_accessor :method_data

  def initialize
    @active = true
  end

  def passes(instance, block, method_name, result, args)
    return true unless method_name == @method_data.method_name && @active

    runner = ConditionRunner.new(instance, @method_data.args.zip(args))
    super(runner, block, method_name, result, args)
  end
end
