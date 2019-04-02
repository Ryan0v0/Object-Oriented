import java.math.BigInteger;
import java.util.ArrayList;

public class CosFactor extends Factor {
    private BigInteger deg;
    
    public CosFactor() {
        super();
    }
    
    public CosFactor(BigInteger deg) {
        super(deg);
    }
    
    //TODO
    public CosFactor(String str) {
        super(str);
    }
    
    @Override
    public Product Derivation() { // sinx^6 6sinx^5cosx
        ArrayList<Factor> fac = new ArrayList<>();
        BigInteger co = getDegree();
        Factor f1 = new CosFactor(getDegree().subtract(BigInteger.ONE));
        if (!getDegree().equals(BigInteger.ZERO)
                && !getDegree().equals(BigInteger.ONE)) {
            fac.add(f1);
        }
        Factor f2 = new SinFactor(BigInteger.ONE);
        if (!getDegree().equals(BigInteger.ZERO)) {
            fac.add(f2);
            co = co.negate();
        }
        Product res = new Product(fac, co);
        return res;
    }
    
    @Override
    public String ToString() {
        StringBuffer str = new StringBuffer();
        if (getDegree().equals(BigInteger.ONE)) {
            str.append("cos(x)");
        } else {
            str.append("cos(x)^");
            str.append(getDegree().toString());
        }
        str.trimToSize();
        return str.toString();
    }
}