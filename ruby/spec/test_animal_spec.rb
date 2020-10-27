describe Animal do
  describe 'PRUEBAS EN CLASES' do
    let(:una_clase) {
      Animal.new
    }

    it 'Clase cumple invariant' do
      una_clase.correr 12
      una_clase.correr 15
      expect(una_clase.energia).to eq(46)
    end

    it 'Clase NO cumple invariant' do
      una_clase.correr 22
      una_clase.correr 15
      expect { una_clase.correr 18 }.to raise_exception ContractException
    end

    it 'Clase cumple precondicion' do
      una_clase.correr 15
      una_clase.correr 25
      una_clase.comer 5
      expect(una_clase.energia).to eq(45)
    end

    it 'Clase NO cumple precondicion' do
      una_clase.correr 21
      una_clase.correr 25
      expect { una_clase.comer 5 }.to raise_exception ContractException
    end

    it 'Clase cumple postcondicion' do
      una_clase.correr 15
      una_clase.comer 3
      expect(una_clase.energia).to eq(85)
    end

    it 'Clase NO cumple postcondicion' do
      una_clase.correr 15
      expect { una_clase.comer 7 }.to raise_exception ContractException
    end
  end

  describe 'PRUEBAS EN SUBCLASES' do
    let(:una_sub) {
      Perro.new
    }

    it 'Subclase cumple invariant de super' do
      una_sub.correr 12
      una_sub.correr 15
      expect(una_sub.energia).to eq(46)
    end

    it 'Subclase NO cumple invariant de super' do
      una_sub.correr 22
      una_sub.correr 15
      expect { una_sub.correr 18 }.to raise_exception ContractException
    end

    it 'Subclase cumple precondicion de super' do
      una_sub.correr 15
      una_sub.correr 25
      una_sub.comer 5
      expect(una_sub.energia).to eq(45)
    end

    it 'Subclase NO cumple precondicion de super' do
      una_sub.correr 21
      una_sub.correr 25
      expect { una_sub.comer 5 }.to raise_exception ContractException
    end

    it 'Subclase cumple postcondicion de super' do
      una_sub.correr 15
      una_sub.comer 3
      expect(una_sub.energia).to eq(85)
    end

    it 'Subclase NO cumple postcondicion de super' do
      una_sub.correr 15
      expect { una_sub.comer 7 }.to raise_exception ContractException
    end
  end

  describe 'PRUEBAS EN SUBCLASE DE SUBCLASE' do
    let(:una_sub_sub) {
      Dogo.new
    }

    it 'Sub-subclase cumple invariant de super' do
      una_sub_sub.correr 12
      una_sub_sub.correr 15
      expect(una_sub_sub.energia).to eq(46)
    end

    it 'Sub-subclase NO cumple invariant de super' do
      una_sub_sub.correr 22
      una_sub_sub.correr 15
      expect { una_sub_sub.correr 18 }.to raise_exception ContractException
    end

    it 'Sub-subclase cumple precondicion de super' do
      una_sub_sub.correr 15
      una_sub_sub.correr 25
      una_sub_sub.comer 5
      expect(una_sub_sub.energia).to eq(45)
    end

    it 'Sub-subclase NO cumple precondicion de super' do
      una_sub_sub.correr 21
      una_sub_sub.correr 25
      expect { una_sub_sub.comer 5 }.to raise_exception ContractException
    end

    it 'Sub-subclase cumple postcondicion de super' do
      una_sub_sub.correr 15
      una_sub_sub.comer 3
      expect(una_sub_sub.energia).to eq(85)
    end

    it 'Sub-subclase NO cumple postcondicion de super' do
      una_sub_sub.correr 15
      expect { una_sub_sub.comer 7 }.to raise_exception ContractException
    end
  end
end