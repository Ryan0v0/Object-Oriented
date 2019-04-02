import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Quantity {
    public abstract Quantity Derivation();
    //TODO
    //protected abstract Quantity Combine(Quantity t2);
    //TODO
    //public abstract Product Combine(Product p) throws ClassCastException;
    
    public static Quantity Classify(String str) {
        if (str.charAt(0) == '(') { //表达式因子
            return new Poly(str.substring(1, str.length() - 1));
        } else { //其他因子
            Pattern r = Pattern.compile("[\\+\\-]?\\d+");
            Matcher m = r.matcher(str);
            if (m.matches()) { //常数因子直接归入乘积项类中
                return new Product(m.group(0));
            } else {
                //if(str.contains("sin")){不可以contains
                if (str.charAt(0) == 's') { //正弦函数因子
                    return new SinNest(str);
                } else if (str.charAt(0) == 'c') { //余弦函数因子
                    return new CosNest(str);
                } else { //剩余情况只可能是幂函数因子
                    return new PowerFactor(str);
                }
            }
        }
    }
}
