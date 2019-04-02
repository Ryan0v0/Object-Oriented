import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poly extends Quantity {
    private ArrayList<Product> productList = new ArrayList<>();
    
    private String matchProduct(String str) {
        String num = "[+-]?\\d+";
        String pow = "x(\\^" + num + ")?";
        String sin = "sin\\(x\\)(\\^" + num + ")?";
        String cos = "cos\\(x\\)(\\^" + num + ")?";
        //String sin = "sin\\((.*)\\)(\\^" + num + ")?";
        //String cos = "cos\\((.*)\\)(\\^" + num + ")?";
        String factor =
                "(" + num + ")|(" + pow + ")|(" + sin + ")|(" + cos + ")";
        String product =
                "^[+-]{1,2}(\\d+\\*)?((" + factor + ")\\*)*+(" + factor + ")";
        Pattern r = Pattern.compile(product);
        Matcher m = r.matcher(str);
        if (m.find()) {
            return m.group();
        } else {
            return "";
        }
    }
    
    public Poly(ArrayList<Product> p) {
        this.productList = new ArrayList<Product>();
        for (int i = 0; i < p.size(); i++) {
            Product t1 = p.get(i);
            boolean isCombine = false;
            for (int j = 0; j < productList.size(); j++) {
                Product t2 = productList.get(j);/*
                if (t1.canCombine(t2)) {
                    productList.set(j, t1.Combine(t2));
                    if (productList.get(j).getCoeff().equals(BigInteger.ZERO)) {
                        productList.remove(j);
                    }
                    isCombine = true;
                    break;
                }*/
            } /*
            if (isCombine) {
                continue;
            }*/
            if (!p.get(i).getCoeff().equals(BigInteger.ZERO)) {
                this.productList.add(p.get(i));
            }
        }
    }
    
    private int cntBracket(String s) {
        int cnt = 0;
        int len = s.length();
        StringBuffer sb = new StringBuffer(s);
        while (sb.charAt(0) == '(' && sb.charAt(len - 1) == ')') {
            sb.delete(len - 1, len);
            sb.delete(0, 1);
            len = sb.length();
            cnt++;
        }
        return cnt;
    }
    
    private String myTrim(String s) {
        int len = s.length();
        StringBuffer sb = new StringBuffer(s);
        while (sb.charAt(0) == '(' && sb.charAt(len - 1) == ')') {
            sb.delete(len - 1, len);
            sb.delete(0, 1);
            len = sb.length();
        }
        return sb.toString();
    }
    
    private boolean checkSpaceLegal(String str1) {
        String str = str1;
        /*String spaceInNum = "(\\d[ \\t]+\\d)" +
                "|([\\*\\^][ \\t]*[\\+\\-][ \\t]+\\d)" +
                "|(([\\+\\-][ \\t]*){2}[\\+\\-][ \\t]+\\d)" +
                "|([ns][ \\t]*\\([ \\t]*[\\+\\-][ \\t]+\\d)";*/
        String spaceInNum = "(\\d[ \\t]+\\d)" +
                "|([\\*\\^][ \\t]*[\\+\\-][ \\t]+\\d)" +
                "|(([\\+\\-][ \\t]*){2}[\\+\\-][ \\t]+\\d)" +
                "|([ns][ \\t]*\\([ \\t]*[\\+\\-][ \\t]+\\d)";
        Pattern r = Pattern.compile(spaceInNum);
        Matcher m = r.matcher(str);
        if (m.find()) {
            return false;
        }
        str = str.replaceAll("sin", "");
        str = str.replaceAll("cos", "");
        r = Pattern.compile("[^x \\t\\d\\^\\+\\-\\*\\(\\)]");
        m = r.matcher(str);
        if (m.find()) {
            return false;
        }
        
        return true;
    }
    
    //递归判断多项式合法性,分解多项式不需要递归
    private boolean isLegal(String str1) {
        String str = str1;
        while (!str.equals("")) {
            String temp = matchProduct(str);
            if (!temp.equals("")) {
                str = str.substring(temp.length());
            } else {
                int cnt = 0;
                int flag = 0;
                boolean nest = false;
                int begin = 0;
                int end = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == '(') {
                        cnt++;
                        if (cnt == 1) {
                            begin = i;
                            if (i > 0 && (str.charAt(i - 1) == 'n' ||
                                    str.charAt(i - 1) == 's')) {
                                nest = true;
                            }
                        }
                    } else if (str.charAt(i) == ')') {
                        cnt--;
                        if (cnt == 0) {
                            end = i;
                            if (!nest) {
                                String newstr = str.substring(begin + 1, end);
                                if (!newstr.equals("") &&
                                        (newstr.charAt(0) != '+' &&
                                                newstr.charAt(0) != '-')) {
                                    newstr = "+" + newstr;
                                }
                                if (isLegal(newstr)) {
                                    str = str.substring(0, begin) + "x" +
                                            str.substring(end + 1);
                                    flag = 1;
                                    break;
                                } else {
                                    return false;
                                }
                            } else if (isFactor(str.substring(
                                    begin + 1, end))) {
                                str = str.substring(0, begin - 3) +
                                        "x" + str.substring(end + 1);
                                flag = 1;
                                break;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                if (flag == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    /*private boolean isLegal(String str){
        if (str.equals("")) {
            return false;
        }
        while (!str.equals("")) {
            String cur = matchProduct(str);
            if (!cur.equals("")) {
                str = str.substring(cur.length());
            } else {
            
            }
    }*/
    
    private String workSignal(String str1) {
        String str = str1;
        str = str.replaceAll("(\\+\\+)|(\\-\\-)", "\\+");
        str = str.replaceAll("(\\+\\-)|(\\-\\+)", "\\-");
        str = str.replaceAll("(\\+\\+)|(\\-\\-)", "\\+");
        str = str.replaceAll("(\\+\\-)|(\\-\\+)", "\\-");
        str = str.replaceAll("\\-", "\\+\\-"); // 减号转化为加号处理
        str = str.replaceAll("\\^\\+", "\\^");
        str = str.replaceAll("\\*\\+", "\\*"); // x*+3的情形!
        if (str.charAt(0) == '+') {
            str = str.replaceFirst("\\+", "");
        }
        return str;
    }
    
    private String splitProduct(String str1) {
        String str = str1;
        int stk = 0;
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c == '(') {
                stk++;
            } else if (c == ')') {
                stk--;
            } else if (stk == 0 && c == '+') {
                return str.substring(0, i);
            }
        }
        return str;
    }
    
    private String init(String str1) {
        String str = str1;
        if (!checkSpaceLegal(str)) {
            throw new NumberFormatException();
        }
        str = str.replaceAll("[ \\t]+", "");
        if (str.equals("")) {
            throw new NumberFormatException();
        }
        if (str.charAt(0) != '+' && str.charAt(0) != '-') {
            str = "+" + str;
        }
        return str;
    }
    
    public Poly(String str1) throws NumberFormatException {
        String str = str1;
        str = init(str);
        if (str.equals("")) {
            throw new NumberFormatException();
        }
        if (!isLegal(str)) {
            throw new NumberFormatException();
        }
        str = workSignal(str);
        this.productList = new ArrayList<Product>();
        while (!str.equals("")) {
            if (str.charAt(0) == '+') {
                str = str.substring(1);
            }
            String cur = splitProduct(str);
            //System.out.println("cur="+cur);
            str = str.substring(cur.length());
            cur = cur.replaceAll("(\\+\\-)|(\\-\\+)", "\\-");
            Product p = new Product(cur);
            boolean isCombine = false;
            for (int i = 0; i < this.productList.size(); i++) {
                Product pro = this.productList.get(i);
                if (p.canCombine(pro)) {
                    this.productList.set(i, p.Combine(pro));
                    if (this.productList.get(i).equals(BigInteger.ZERO)) {
                        this.productList.remove(i);//
                    }
                    isCombine = true;
                    break;
                }
            }
            if (isCombine) {
                continue;
            }
            //this.productList.add(p);
            if (!p.getCoeff().equals(BigInteger.ZERO)) {
                productList.add(p);/*
                System.out.println("~~~~***~~~~");
                System.out.println(productList.size());
                for(Product t:productList) {
                    System.out.println("***");
                    System.out.println(t.toString());
                }*/
            }
        }
    }
    
    
    //是否是合法的嵌套因子
    private boolean isFactor(String str1) {
        if (str1.equals("")) {
            return false;
        }
        String str = str1;
        if (str.charAt(0) == '(') {
            int cnt = 0;
            int pos = -1;
            for (int i = 0; i < str.length(); i++) {
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
            if (pos == str.length() - 1) //表达式因子
            {
                String newstr = str.substring(1, str.length() - 1);
                if (!newstr.equals("") && newstr.charAt(0) != '+' &&
                        newstr.charAt(0) != '-') {
                    newstr = "+" + newstr;
                }
                return isLegal(newstr);
            }
        }
        
        String num = "[+-]?\\d+";
        String pow = "x(\\^" + num + ")?";
        String factor = "(" + num + ")|(" + pow + ")";
        //正则表达式的组命名
        String sin = "(sin\\((?<quad>.*)\\)(\\^" + num + ")?)";
        String cos = "(cos\\((?<quad>.*)\\)(\\^" + num + ")?)";
        
        //常数、幂函数因子
        Pattern r = Pattern.compile(factor);
        Matcher m = r.matcher(str);
        if (m.matches()) {
            return true;
        } else { //三角函数因子
            r = Pattern.compile(sin);
            m = r.matcher(str);
            if (m.matches()) {
                return isFactor(m.group("quad"));
            } else {
                r = Pattern.compile(cos);
                m = r.matcher(str);
                if (m.matches()) {
                    return isFactor(m.group("quad"));
                } else {
                    return false;
                }
            }
        }
    }
    
    public Product Derivation() {
        ArrayList<Product> p = new ArrayList<Product>();
        for (int i = 0; i < this.productList.size(); i++) {
            p.addAll(this.productList.get(i).Derivation().productList);
        }
        Poly res = new Poly(p);
        ArrayList<Quantity> resList = new ArrayList<>();
        resList.add(res);
        Product resPro = new Product(resList, BigInteger.ONE);
        return resPro;
    }
    
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        int flag = -1;
        int cnt = 0;/*
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getCoeff().compareTo(BigInteger.ZERO) > 0) {
                flag = i;
                break;
            }
        }
        if (flag != -1) {
            Product tmp = productList.get(flag);
            str.append(tmp.toString());
            cnt++;
            productList.remove(flag);
        }*/
        for (int i = 0; i < productList.size(); i++) { // new size
            Product cur = productList.get(i);
            //if (cnt > 0 && cur.getCoeff().compareTo(BigInteger.ZERO) == 1) {
            if (cur.getCoeff().compareTo(BigInteger.ZERO) == 1) {
                str.append("+");
            }
            str.append(productList.get(i).toString());
            cnt++;
        }
        str.trimToSize();
        //if (str.length() == 0) {
        if (str.toString().equals("")) {
            //str.append("(0)");
            return "(0)";
        }
        if (str.charAt(0) == '+') {
            str.deleteCharAt(0);
        }
        return "(" + str.toString() + ")";
    }
    
    public ArrayList<Product> getList() {
        return (ArrayList<Product>) productList.clone();
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
        Poly obj = (Poly) ob;
        if (this.productList.size() != obj.productList.size()) {
            return false;
        }
        //ArrayList<Product> templ = obj.getList();
        for (Product p1 : productList) {
            boolean flag = false;
            for (Product p2 : obj.getList()) {
                if (p1.equals(p2)) {
                    flag = true;
                    //templ.remove(tt);
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
}
