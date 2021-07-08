package aoizora.atm.cassette;

import aoizora.atm.Banknote;
import aoizora.atm.types.Cassette;
import aoizora.atm.visitor.CassetteVisitor;

import java.util.ArrayList;
import java.util.List;

public class TenCassette implements Cassette {

    private int count;

    public TenCassette(int count) {
        this.count = count;
    }

    @Override
    public void insert(Banknote banknote) {
        if (!banknote.equals(Banknote.TEN))
            throw new RuntimeException("Invalid banknote value: " + banknote);
        this.count++;
    }

    @Override
    public List<Banknote> withdraw(int count) {

        if (this.count < count)
            throw new RuntimeException("Not enough banknotes");

        List<Banknote> result = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            result.add(Banknote.TEN);
            this.count--;
        }
        return result;
    }

    @Override
    public int getBanknoteCount() {
        return count;
    }

    @Override
    public Banknote getBanknoteType() {
        return Banknote.TEN;
    }

    @Override
    public long accept(CassetteVisitor visitor) {
        return visitor.visit(this);
    }
}
