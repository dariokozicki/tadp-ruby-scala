describe Contrato do
  before do
    @pila = Pila.new 50
  end

  describe '#pila' do
    it 'initializes as an empty queue' do
      puts @pila.empty?
    end
  end
end