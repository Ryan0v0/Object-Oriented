import java.math.BigInteger;
import java.util.ArrayList;

public class CosNest extends Factor {
    private BigInteger deg; // 指数
    private Quantity quant;
    
    public CosNest() {
        super();
    }
    
    public CosNest(BigInteger degree, Quantity quantity) {
        super(degree);
        quant = quantity;
    }
    
    //保证合法的情况下.不需要抛出异常
    public CosNest(String str) {
        super(str);
        int cnt = 0;
        int pos = -1;
        for (int i = 3; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                cnt++;
            } else if (str.charAt(i) == ')') {
                cnt--;
                if (cnt == 0) {
                    pos = i;
                    break;
                }
            }
        }
        quant = Quantity.Classify(str.substring(4, pos));
    }
    
    public Quantity getQuant() {
        return quant;
    }
    
    public Product Derivation() { // sinx^6 6sinx^5cosx
        ArrayList<Quantity> fac = new ArrayList<>();
        Quantity f1 = new CosNest(getDegree().subtract(BigInteger.ONE), quant);
        if (!getDegree().equals(BigInteger.ZERO)
                && !getDegree().equals(BigInteger.ONE)) {
            fac.add(f1);
        }
        Quantity f2 = new SinNest(BigInteger.ONE, quant);
        if (!getDegree().equals(BigInteger.ZERO)) {
            fac.add(f2);
        }
        fac.add(quant.Derivation());
        BigInteger co = getDegree().negate();
        Product res = new Product(fac, co);
        return res;
    }
    
    public boolean canCombine(Quantity ob) {
        if (this.getClass() == ob.getClass() &&
                getDegree().equals(((Factor) ob).getDegree()) &&
                quant.equals(((CosNest) ob).quant)) {
            return true;
        }
        return false;
    }
    
    public Factor Combine(Factor ob) throws ClassCastException {
        if (this.canCombine(ob)) {
            CosNest res = new CosNest(getDegree(), getQuant());
            return res;
        } else {
            throw new ClassCastException();
        }
    }
    
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        //指数为0?
        str.append("cos(");
        str.append(quant.toString());
        str.append(")");
        if (!getDegree().equals(BigInteger.ONE)) {
            str.append("^");
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
        CosNest obj = (CosNest) ob;
        return getDegree().equals(obj.getDegree())
                && quant.equals(obj.quant);
    }
}