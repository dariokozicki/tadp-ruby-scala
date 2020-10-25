Dir['./lib/*'].sort.each { |file| require file }

class EachCall
  include ConditionalStrategy
  def passes(instance, block, method_name, *arg)
    res = instance.instance_exec(*arg, &block)
    raiseOnFalse res
  end

  def method_name=(method_name)
    nil #do nothing
  end

  def method_name
    'Undefined'
  end
end
