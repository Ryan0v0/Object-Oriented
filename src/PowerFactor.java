import java.math.BigInteger;
import java.util.ArrayList;

public class PowerFactor extends Factor {
    //    private BigInteger co; // 系数
    private BigInteger deg; // 指数
    
    public PowerFactor() {
        super();
    }
    
    public PowerFactor(BigInteger deg) {
        super(deg);
    }
    
    public PowerFactor(String str) {
        super(str);
    }
    
    @Override
    public Product Derivation() {
        ArrayList<Quantity> fac = new ArrayList<>();
        BigInteger deg = getDegree();//
        if (!deg.equals(BigInteger.ONE) && !deg.equals(BigInteger.ZERO)) {
            Factor f = new PowerFactor(deg.subtract(BigInteger.ONE));
            fac.add(f);
        }
        Product res = new Product(fac, deg);
        return res;
    }
    
    public boolean canCombine(Quantity ob) {
        if (this.getClass() == ob.getClass() &&
                getDegree().equals(((Factor) ob).getDegree())) {
            return true;
        }
        return false;
    }
    
    public Factor Combine(Factor ob) throws ClassCastException {
        if (this.canCombine(ob)) {
            PowerFactor res = new PowerFactor(getDegree().add(ob.getDegree()));
            return res;
        } else {
            throw new ClassCastException();
        }
    }
    
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (getDegree().equals(BigInteger.ONE)) {
            str.append("x");
        } else {
            str.append("x^");
            str.append(getDegree().toString());
        }
        str.trimToSize();
        return str.toString();
    }
    
    @Override
    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (ob == null) {
            return false;
        }
        if (getClass() != ob.getClass()) {
            return false;
        }
        PowerFactor other = (PowerFactor) ob;
        return getDegree().equals(other.getDegree());
    }
}
