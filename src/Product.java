import java.math.BigInteger;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product extends Quantity {
    private BigInteger co;
    private ArrayList<Quantity> factorList;// = new ArrayList<Factor>();
    
    private String[] getRegex() {
        String[] regexs = new String[5];
        regexs[0] = "([+-]?\\d+)+";//num!!!
        regexs[1] = "(x(\\^" + regexs[0] + ")?)+";//pow
        regexs[2] = "(sin\\((.*)\\)(\\^" + regexs[0] + ")?)+";
        regexs[3] = "(cos\\((.*)\\)(\\^" + regexs[0] + ")?)+";
        return regexs;
    }
    
    private void work(Factor cur1) {
        boolean canCombine = false;
        for (int k = 0; k < factorList.size(); k++) {
            Factor cur = (Factor) factorList.get(k);
            if (cur.getClass() == cur1.getClass()) {
                //canCombine findsamefact
                factorList.set(k, cur.Combine(cur1));
                if (((Factor) factorList.get(k)).getDegree().equals(
                        (BigInteger.ZERO))) {
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
        factorList = new ArrayList<Quantity>();
        //String[] items = str.split("\\*");//不可以直接用乘号分割!
        ArrayList<String> temp = new ArrayList<>();
        int start = 0;
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                cnt++;
            } else if (str.charAt(i) == ')') {
                cnt--;
            } else if (cnt == 0 && str.charAt(i) == '*') {
                temp.add(str.substring(start, i));
                start = i + 1;
            }
        }
        temp.add(str.substring(start));
        String[] items = temp.toArray(new String[temp.size()]);
        /*System.out.println("!!!!!!");
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i]);
        }
        System.out.println("!!!!!!");*/
        if (items[0].charAt(0) == '+' || items[0].charAt(0) == '-') {
            if (items[0].charAt(0) == '-') {
                co = co.negate();
            }
            items[0] = items[0].substring(1);
        }
        String[] regexs = this.getRegex();
        
        for (int i = 0; i < items.length; i++) {
            Pattern r = Pattern.compile(regexs[0]);
            Matcher m = r.matcher(items[i]);
            if (m.matches()) {
                co = co.multiply(new BigInteger(items[i]));
                continue;
            }
            Quantity cur = Factor.Classify(items[i]);//调用静态工厂方法,实现封装
            //work((Factor)cur);
            if (cur instanceof Poly || cur instanceof Factor &&
                    !((Factor) cur).getDegree().equals(BigInteger.ZERO)) {
                factorList.add(cur);
            }
        } /*
        
        for (int i = 0; i < items.length; i++) {
            //for (int j = 0; j < 4; j++) {
            for (int j = 3; j >= 0; j--) {
                Pattern r = Pattern.compile(regexs[j]);
                Matcher m = r.matcher(items[i]);
                if (m.find()) {
                    System.out.println(j);
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
                            System.out.println("~~~~~~~");
                            System.out.println(m.group());
                            System.out.println("~~~~~~~");
                            Factor cur2 = new SinNest(m.group());
                            work(cur2);
                            break;
                        case 3:
                            Factor cur3 = new CosNest(m.group());
                            work(cur3);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                
            }
        }*/
    }
    
    public Product(ArrayList<Quantity> pro, BigInteger coeff) {
        co = coeff;
        factorList = new ArrayList<Quantity>();
        ListIterator<Quantity> it = pro.listIterator();
        while (it.hasNext()) {
            Quantity cur = it.next();
            if (cur instanceof Product) {
                Product p = (Product) cur;
                co = co.multiply(p.getCoeff());
                for (Quantity tmp : p.getList()) {
                    it.add(tmp);
                    it.previous();
                }
                continue;
            }
            if (cur instanceof Poly || cur instanceof Factor &&
                    !((Factor) cur).getDegree().equals(BigInteger.ZERO)) {
                factorList.add(cur);
            }
        }
    }
    
    public BigInteger getCoeff() {
        return co;
    }
    
    public ArrayList<Quantity> getList() {
        return (ArrayList<Quantity>) factorList.clone();
    }
    
    public Poly Derivation() {
        ArrayList<Product> p = new ArrayList<>();
        for (int i = 0; i < factorList.size(); i++) {
            BigInteger coeff = this.co;
            ArrayList<Quantity> fac = (ArrayList<Quantity>)
                    this.factorList.clone();
            fac.remove(i);
            Product pro = (Product) factorList.get(i).Derivation();
            coeff = coeff.multiply(pro.co);
            fac.addAll(pro.factorList);
            Product resPro = new Product(fac, coeff);
            p.add(resPro);
        }
        Poly resPoly = new Poly(p);
        return resPoly;
    }
    
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (factorList.size() == 0) { //只有常数项
            str.append(this.co.toString());
        } else {
            if (this.co.equals(BigInteger.ONE)) {
                for (int i = 0; i < factorList.size(); i++) {
                    if (i > 0) {
                        str.append("*");
                    }
                    str.append(factorList.get(i).toString());
                }
            } else if (this.co.equals(BigInteger.ONE.negate())) {
                str.append("-");
                for (int i = 0; i < factorList.size(); i++) {
                    if (i > 0) {
                        str.append("*");
                    }
                    str.append(factorList.get(i).toString());
                }
            } else if (!this.co.equals(BigInteger.ZERO)) {
                str.append(this.co.toString());
                str.append("*");
                for (int i = 0; i < factorList.size(); i++) {
                    if (i > 0) {
                        str.append("*");
                    }
                    str.append(factorList.get(i).toString());
                }
            }
        }
        str.trimToSize();
        if (str.length() == 0) {
            //str.append("");
            return "";
        }
        if (str.charAt(0) == '+') {
            str.deleteCharAt(0);
        }
        return str.toString();
    }
    
    public boolean canCombine(Product ob) {
        if (this.factorList.size() != ob.factorList.size()) { // 是否含有三个元素
            return false;
        } else { // 三个元素对应参数是否相同
            for (int i = 0; i < this.factorList.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < ob.factorList.size(); j++) {
                    Quantity t1 = this.factorList.get(i);
                    Quantity t2 = ob.factorList.get(j);
                    if (t1.equals(t2)) {
                        /*if (t1.getClass() == t2.getClass()
                            && t1.getDegree().equals(t2.getDegree())) {*/
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
        Product other = (Product) ob;
        return co.equals(other.co) && canCombine(other);
    }
}
