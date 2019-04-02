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
        ArrayList<Factor> fac = new ArrayList<>();
        BigInteger deg = getDegree();//
        if (!deg.equals(BigInteger.ONE) && !deg.equals(BigInteger.ZERO)) {
            Factor f = new PowerFactor(deg.subtract(BigInteger.ONE));
            fac.add(f);
        }
        Product res = new Product(fac, deg);
        return res;
    }
    
    @Override
    public String ToString() {
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
    
}
