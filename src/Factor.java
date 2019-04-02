import java.math.BigInteger;
import java.util.ArrayList;

public abstract class Factor extends Quantity {
    private BigInteger deg; // 指数
    
    public Factor() {
        deg = BigInteger.ZERO; //初始化为单位1
    }
    
    public Factor(String str) throws NumberFormatException {
        //最好不要在构造函数中抛出异常
        int start = 0;
        int cnt = 0;
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                cnt++;
            } else if (str.charAt(i) == ')') {
                cnt--;
            } else if (cnt == 0 && str.charAt(i) == '^') {
                temp.add(str.substring(start, i));
                start = i + 1;
            }
        }
        temp.add(str.substring(start));
        //if (str.contains("^")) {
        if (start != 0) {
            //String[] buf = str.split("\\^");
            String[] buf = temp.toArray(new String[temp.size()]);
            /*System.out.println("~~~~~~~");
            for(int i=0;i<buf.length;i++)
                System.out.println(buf[i]);
            System.out.println("~~~~~~~");*/
            deg = new BigInteger(buf[1]);
        } else {
            deg = BigInteger.ONE;
        }
        if (deg.compareTo(BigInteger.valueOf(10000)) > 0) {
            throw new NumberFormatException();
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
    
    public abstract String toString();
    
    public abstract boolean canCombine(Quantity fac);
    
    public abstract Factor Combine(Factor fac);
    /*public Factor Combine(Factor fac) throws ClassCastException{
        if (this.getClass() == fac.getClass()) {
            if (this.getClass().getName().equals("PowerFactor")) {
                Factor res =
                        new PowerFactor(this.getDegree().add(fac.getDegree()));
                return res;
            } else if (this.getClass().getName().equals("SinNest")
                            &&(SinNest)this.) {
                Factor res =
                        new SinNest(this.getDegree().add(fac.getDegree()));
                return res;
            } else if (this.getClass().getName().equals("CosNest")) {
                Factor res =
                        new CosNest(this.getDegree().add(fac.getDegree()));
                return res;
            } else {
                throw new ClassCastException();
            }
        
        } else {
            throw new ClassCastException();
        }
    }*/
    
    /*TODO
    public static Factor Classify() {
    
    }*/
    
}
