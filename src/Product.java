import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product {
    private BigInteger co;
    private ArrayList<Factor> factorList;// = new ArrayList<Factor>();
    
    private String[] getRegex() {
        String[] regexs = new String[5];
        regexs[0] = "([+-]?\\d+)+";//num
        regexs[1] = "(x(\\^" + regexs[0] + ")?)+";//pow
        regexs[2] = "(sin\\(x\\)(\\^" + regexs[0] + ")?)+";
        regexs[3] = "(cos\\(x\\)(\\^" + regexs[0] + ")?)+";
        return regexs;
    }
    
    private void work(Factor cur1) {
        boolean canCombine = false;
        for (int k = 0; k < factorList.size(); k++) {
            Factor cur = factorList.get(k);
            if (cur.getClass() == cur1.getClass()) {
                //canCombine
                factorList.set(k, cur.Combine(cur1));
                if (factorList.get(k).getDegree().equals((BigInteger.ZERO))) {
                    factorList.remove(k);
                }
                canCombine = true;
                break;
            }
        }
        if (!canCombine) {
            if (!cur1.getDegree().equals(BigInteger.ZERO)) {
                factorList.add(cur1);
            }
        }
    }
    
    public Product(String str) {
        co = BigInteger.ONE;
        factorList = new ArrayList<Factor>();
        String[] items = str.split("\\*");
        if (items[0].charAt(0) == '+' || items[0].charAt(0) == '-') {
            if (items[0].charAt(0) == '-') {
                co = co.negate();
            }
            items[0] = items[0].substring(1);
        }
        String[] regexs = this.getRegex();
        for (int i = 0; i < items.length; i++) {
            //for (int j = 0; j < 4; j++) {
            for (int j = 3; j >= 0; j--) {
                Pattern r = Pattern.compile(regexs[j]);
                Matcher m = r.matcher(items[i]);
                if (m.find()) {
                    switch (j) {
                        case 0:
                            BigInteger num = new BigInteger(m.group());
                            co = co.multiply(num);
                            break;
                        case 1:
                            Factor cur1 = new PowerFactor(m.group());
                            work(cur1);
                            break;
                        case 2:
                            Factor cur2 = new SinFactor(m.group());
                            work(cur2);
                            break;
                        case 3:
                            Factor cur3 = new CosFactor(m.group());
                            work(cur3);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                
            }
        }
    }
    
    public Product(ArrayList<Factor> pro, BigInteger coeff) {
        this.co = coeff;
        factorList = new ArrayList<Factor>();
        for (int i = 0; i < pro.size(); i++) {
            Factor t1 = pro.get(i);
            boolean canCombine = false;
            for (int j = 0; j < factorList.size(); j++) {
                Factor t2 = factorList.get(j);
                if (t1.getClass() == t2.getClass()) { //canCombine
                    factorList.set(j, t1.Combine(t2));
                    if (factorList.get(j).getDegree().equals(
                            (BigInteger.ZERO))) {
                        factorList.remove(j);
                    }
                    canCombine = true;
                    break;
                }
            }
            if (!canCombine) {
                if (!pro.get(i).getDegree().equals(BigInteger.ZERO)) {
                    factorList.add(pro.get(i));
                }
            }
        }
    }
    
    public BigInteger getCoeff() {
        return co;
    }
    
    public Poly Derivation() {
        ArrayList<Product> p = new ArrayList<>();
        for (int i = 0; i < factorList.size(); i++) {
            BigInteger coeff = this.co;
            ArrayList<Factor> fac = (ArrayList<Factor>) this.factorList.clone();
            fac.remove(i);
            Product pro = factorList.get(i).Derivation();
            coeff = coeff.multiply(pro.co);
            fac.addAll(pro.factorList);
            Product resPro = new Product(fac, coeff);
            p.add(resPro);
        }
        Poly resPoly = new Poly(p);
        return resPoly;
    }
    
    public String ToString() {
        StringBuffer str = new StringBuffer();
        if (factorList.size() == 0) { //只有常数项
            str.append(this.co.toString());
        } else {
            if (this.co.equals(BigInteger.ONE)) {
                for (int i = 0; i < factorList.size(); i++) {
                    if (i > 0) {
                        str.append("*");
                    }
                    str.append(factorList.get(i).ToString());
                }
            } else if (this.co.equals(BigInteger.ONE.negate())) {
                str.append("-");
                for (int i = 0; i < factorList.size(); i++) {
                    if (i > 0) {
                        str.append("*");
                    }
                    str.append(factorList.get(i).ToString());
                }
            } else if (!this.co.equals(BigInteger.ZERO)) {
                str.append(this.co.toString());
                str.append("*");
                for (int i = 0; i < factorList.size(); i++) {
                    if (i > 0) {
                        str.append("*");
                    }
                    str.append(factorList.get(i).ToString());
                }
            }
        }
        str.trimToSize();
        if (str.length() == 0) {
            str.append("");
        }
        if (str.charAt(0) == '+') {
            str.deleteCharAt(0);
        }
        return str.toString();
    }
    
    public boolean canCombine(Product p) {
        if (this.factorList.size() != p.factorList.size()) { // 是否含有三个元素
            return false;
        } else { // 三个元素对应参数是否相同
            for (int i = 0; i < this.factorList.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < p.factorList.size(); j++) {
                    Factor t1 = this.factorList.get(i);
                    Factor t2 = p.factorList.get(j);
                    if (t1.getClass() == t2.getClass()
                            && t1.getDegree().equals(t2.getDegree())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Product Combine(Product p) throws ClassCastException { //三元组合并
        if (this.canCombine(p)) { // 有相同参数的三元组是同类项
            Product res = new Product(this.factorList, this.co.add(p.co));
            return res;
        } else {
            throw new ClassCastException();
        }
    }
}
