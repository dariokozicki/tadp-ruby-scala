class Inactive
  attr_accessor :condition
  def initialize(condition)
    @condition = condition
  end

  def passes(*_args, &_block)
    @condition.active_state = Active.new(@condition)
    nil
  end

end
