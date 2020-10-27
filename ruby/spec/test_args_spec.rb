# frozen_string_literal: true

describe Operaciones do
  before do
    @operaciones = Operaciones.new
  end

  describe '#Argumentos' do

    it 'Prueba de division con 5' do
      expect(@operaciones.dividir(20, 5) == 4).to eql true
    end

    it 'Prueba de division con 0' do
      expect {@operaciones.dividir(20, 0) == 2}.to raise_exception(ContractException)
    end

    it 'Prueba de division con 10' do
      #@operaciones.dividir(20,10)
      expect(@operaciones.dividir(20, 10) == 2).to eql true
    end


  end
end