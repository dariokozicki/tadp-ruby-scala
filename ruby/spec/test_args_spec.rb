# frozen_string_literal: true

describe Operaciones do
  before do
    @operaciones = Operaciones.new(10,20)
  end

  describe '#Argunmentos' do
    it 'Prueba de division' do
      @operaciones.dividir(20,10)
      expect(@operaciones.dividir(20,10) == 2).to eql true
    end


    # it 'fails to initialize with negative capacity' do
    #expect { Pila.new(-1) }.to raise_exception(InvariantException)
    #end


  end
end
