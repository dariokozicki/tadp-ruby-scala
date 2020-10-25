# frozen_string_literal: true

Dir['./lib/*'].sort.each { |file| require file }
module Contract
  module ClassMethods
    attr_writer :preconditions, :postconditions, :methods_redefined
    def preconditions
      @preconditions ||= []
    end

    def postconditions
      @postconditions ||= []
    end

    def methods_redefined
      @methods_redefined ||= []
    end

    def method_added(method_name)
      return if methods_redefined.include? method_name

      methods_redefined << method_name
      old_method = instance_method(method_name)
      define_method(method_name) do |*arg|
        self.class.preconditions.all? { |precond| precond.passes(self, method_name, *arg) }

        result = old_method.bind(self).call(*arg)
        self.class.postconditions.all? { |postcond| postcond.passes(self, method_name, *arg) }

        result
      end
    end

    def before_and_after_each_method(precondition_block, postcondition_block)
      pre(&precondition_block)
      post(&postcondition_block)
    end

    def pre(&before_block)
      preconditions << Precondition.new(before_block, EachCall.new)
    end

    def post(&after_block)
      postconditions << Postcondition.new(after_block, EachCall.new)
    end

    def invariant(&block)
      post(&block)
    end

    def attr_accessor(*args)
      methods_redefined.push(*args)
      super(*args)
    end
  end

  def self.included(base)
    base.extend(ClassMethods)
  end
end
