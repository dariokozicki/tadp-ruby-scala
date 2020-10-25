# frozen_string_literal: true

describe Contract do
  before do
    @pila = Pila.new 5
  end

  describe 'persona' do

    it 'modifies methods' do
      expect(Persona.new(50).hablar).to eql 'Hola'
    end

    it 'breaks when initialized on -1' do
      expect { Persona.new(-1) }.to raise_exception ContractException
    end

    it 'breaks when persona runs' do
      expect { Persona.new(50).correr }.to raise_exception ContractException
    end
  end


  describe '#pila' do
    it 'fails to initialize with negative capacity' do
      expect { Pila.new(-1) }.to raise_exception(ContractException)
    end
    it 'initializes as an empty queue' do
      expect(@pila.empty?).to eql true
    end
    it 'fails to pop without elements' do
      expect { @pila.pop }.to raise_exception(ContractException)
    end
    it 'pushes while not full' do
      [*0...5].each { |num| @pila.push num }
    end
    it 'fails to push while full' do
      expect { [*0...6].each { |num| @pila.push num } }.to raise_exception(ContractException)
    end
    it 'gives top of queue' do
      @pila.push 1
      expect(@pila.top).to eql 1
    end
    it 'fails to give top while empty' do
      expect{ @pila.top }.to raise_exception(ContractException)
    end
    it 'gives height when asked' do
      expect(@pila.height).to eql 0
      @pila.push 1
      expect(@pila.height).to eql 1
    end
    it 'is full when it reaches capacity' do
      expect(@pila.full?).to eql false
      [*0...5].each { |num| @pila.push num }
      expect(@pila.full?).to eql true
    end
  end
end
