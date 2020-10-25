# frozen_string_literal: true

describe Contract do
  describe 'contrato' do

    it 'modifies methods' do
      expect(Persona.new(50).hablar).to eql 'Hola'
    end

    it 'breaks when invariant' do
      expect { Persona.new(-1) }.to raise_exception ContractException
    end

    it 'breaks when persona runs' do
      expect { Persona.new(50).correr }.to raise_exception ContractException
    end
  end
end
