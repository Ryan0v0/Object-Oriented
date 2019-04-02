import java.math.BigInteger;
import java.util.ArrayList;

public class SinFactor extends Factor {
    private BigInteger deg; // 指数
    
    public SinFactor() {
        super();
    }
    
    public SinFactor(BigInteger deg) {
        super(deg);
    }
    
    //TODO
    public SinFactor(String str) {
        super(str);
    }
    
    @Override
    public Product Derivation() { // sinx^6 6sinx^5cosx
        ArrayList<Factor> fac = new ArrayList<>();
        Factor f1 = new SinFactor(getDegree().subtract(BigInteger.ONE));
        if (!getDegree().equals(BigInteger.ZERO)
                && !getDegree().equals(BigInteger.ONE)) {
            fac.add(f1);
        }
        Factor f2 = new CosFactor(BigInteger.ONE);
        if (!getDegree().equals(BigInteger.ZERO)) {
            fac.add(f2);
        }
        BigInteger co = getDegree();
        Product res = new Product(fac, co);
        return res;
    }
    
    @Override
    public String ToString() {
        StringBuffer str = new StringBuffer();
        if (getDegree().equals(BigInteger.ONE)) {
            str.append("sin(x)");
        } else {
            str.append("sin(x)^");
            str.append(getDegree().toString());
        }
        str.trimToSize(); //?
        return str.toString();
    }
}