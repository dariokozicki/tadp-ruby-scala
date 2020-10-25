Dir['./lib/*'].sort.each { |file| require file }

class EachCall
  include ConditionalStrategy
  def passes(instance, block, method_name, *arg)
    res = instance.instance_exec(*arg, &block)
    raiseOnFalse res
  end
end
