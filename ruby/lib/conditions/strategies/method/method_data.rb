class MethodData
  attr_accessor :method_name, :args

  def initialize(method_name, args)
    @method_name = method_name
    @args = args
  end
end
