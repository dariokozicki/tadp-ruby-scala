# frozen_string_literal: true

module Contrato
  attr_accessor :precondicion, :postcondicion, :invariants, :methods_redefined

  def attr_accessor(*args)
    @methods_redefined ||= [:initialize]
    @methods_redefined.push(*args)
    super(*args)
  end

  def evaluar_invariantes(contexto)
    @invariants ||=[]
    raise Exception, 'Failed to meet invariants' unless @invariants.all? { |bloque| contexto.instance_eval(&bloque) }
  end

  def invariant(&bloque_invariant)
    @invariants ||= []
    @invariants << bloque_invariant unless @invariants.include?(bloque_invariant)
  end

  def pre(&antes_bloque)
    @precondicion ||= proc {}
    @precondicion = antes_bloque
  end

  def post(&despues_bloque)
    @postcondicion ||= proc {}
    @postcondicion = despues_bloque
  end

  def method_added(method_name)
    @precondicion ||= self.superclass.precondicion
    @postcondicion ||= self.superclass.postcondicion
    @invariants ||= self.superclass.invariants
    @methods_redefined ||= %i[initialize method_name alias_matcher]
    return if @methods_redefined.include?(method_name)

    new_method = instance_method(method_name)
    @methods_redefined << method_name
    define_method(method_name) do |*arg|
      cumple_pre = self.class.precondicion.nil? || instance_eval(&self.class.precondicion)
      raise Exception, 'Failed to meet precondition' if !cumple_pre.nil? && !cumple_pre

      result = new_method.bind(self).call(*arg)
      cumple_post = self.class.postcondicion.nil? || instance_eval(&self.class.postcondicion)
      raise Exception, 'Failed to meet postcondition' if !cumple_post.nil? && !cumple_post

      self.class.evaluar_invariantes(self)
      return result
    end
  end

  def self.activate
    Module.prepend Contrato
  end
end

