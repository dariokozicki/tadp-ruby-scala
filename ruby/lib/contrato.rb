# frozen_string_literal: true
require_relative '../lib/contract_exception'
require_relative '../lib/invariant_exception'

module Contrato
  attr_accessor :precondicion, :postcondicion, :invariants, :methods_redefined

  def attr_accessor(*args)
    @methods_redefined ||= []
    @methods_redefined.push(*args)
    super(*args)
  end

  def evaluar_invariantes(contexto)
    @invariants ||= []
    raise InvariantException, 'Failed to meet invariants' unless @invariants.all? { |bloque| contexto.instance_eval(&bloque) }
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
    if !self.name.nil?  && respond_to?(:superclass) && !self.superclass.nil?
      #Se agregan las invariantes de la superclase
      if self.invariants.nil? && !self.superclass.invariants.nil?
        self.invariants ||= []
        self.invariants = self.invariants + self.superclass.invariants
      end

      @methods_redefined ||= %i[method_name alias_matcher]
      return if @methods_redefined.include?(method_name)
      new_method = instance_method(method_name)

      aux = new_method.parameters
      if aux != [] && aux[0].size > 1 && @precondicion != nil
        parameters = Hash.new
        aux.each do |elemento|
          parameters[elemento.last] = nil
        end
      end

      @methods_redefined << method_name
      pre_block = @precondicion
      post_block = @postcondicion
      @precondicion = nil
      @postcondicion = nil
      define_method(method_name) do |*arg|

        #Valido que el metodo se llama con argumentos y tengo precondicion => asi poder llenar el valor a los parametros
        if arg != [] && pre_block != nil
          parameters.each_with_index do |(key,value),index|
            parameters[key] = arg[index]
          end
        end

        # Los parametros valido que no esten en la instancia con el mismo nombre y los creo con los valores
        if !pre_block.nil?
          variablesInstancia = self.instance_variables
          if !parameters.nil?
            parameters.keys.each do |key|
              if !variablesInstancia.include? key
                self.singleton_class.send(:attr_accessor, key)

                keyAux = key.to_s
                variable = "@" + keyAux

                self.instance_variable_set(variable.to_sym,parameters[key])
              end
            end
          end
        end

        cumple_pre = pre_block.nil? || instance_eval(&pre_block)
        raise ContractException, 'Failed to meet precondition' if !cumple_pre.nil? && !cumple_pre

        result = new_method.bind(self).call(*arg)
        cumple_post = post_block.nil? || instance_exec(result, &post_block)
        raise ContractException, 'Failed to meet postcondition' if !cumple_post.nil? && !cumple_post

        self.class.evaluar_invariantes(self)
        return result
      end
    end
  end

  def self.activate
    Module.prepend Contrato
  end
end


