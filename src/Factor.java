import java.math.BigInteger;

public abstract class Factor {
    private BigInteger deg; // 指数
    
    public Factor() {
        deg = BigInteger.ZERO; //初始化为单位1
    }
    
    public Factor(String str) {
        if (str.contains("^")) {
            String[] buf = str.split("\\^");
            deg = new BigInteger(buf[1]);
        } else {
            deg = BigInteger.ONE;
        }
    }
    
    public Factor(BigInteger deg) {
        this.deg = deg;
    }
    
    public BigInteger getDegree() {
        return deg;
    }
    
    //抽象基类:
    public abstract Product Derivation();
    
    public abstract String ToString();
    
    //    public boolean canCombine(Factor fac){}
    
    public Factor Combine(Factor fac) throws ClassCastException {
        if (this.getClass() == fac.getClass()) {
            if (this.getClass().getName().equals("PowerFactor")) {
                Factor res =
                        new PowerFactor(this.getDegree().add(fac.getDegree()));
                return res;
            } else if (this.getClass().getName().equals("SinFactor")) {
                Factor res =
                        new SinFactor(this.getDegree().add(fac.getDegree()));
                return res;
            } else if (this.getClass().getName().equals("CosFactor")) {
                Factor res =
                        new CosFactor(this.getDegree().add(fac.getDegree()));
                return res;
            } else {
                throw new ClassCastException();
            }
            
        } else {
            throw new ClassCastException();
        }
    }
    
    /*TODO
    public static Factor Classify() {
    
    }*/
    
}
