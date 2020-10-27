# frozen_string_literal: true

class ConditionRunner
  def initialize(instance, arg_pairs)
    @instance = instance
    arg_pairs.each do |pair|
      define_singleton_method(pair[0]) do
        pair[1]
      end
    end
  end

  def method_missing(method_name, *args, &block)
    @instance.send(method_name, *args, &block)
  end

  def respond_to_missing?(method_name)
    @instance.respond_to? method_name
  end
end
