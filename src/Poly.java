import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poly {
    private ArrayList<Product> productList;
    
    private String matchProduct(String str) {
        String num = "[+-]?\\d+";
        String pow = "x(\\^" + num + ")?";
        String sin = "sin\\(x\\)(\\^" + num + ")?";
        String cos = "cos\\(x\\)(\\^" + num + ")?";
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
    
    private boolean judgeFormat(String str) {
        String num = "[ \\t]*[+-]?\\d+[ \\t]*";
        String pow = "[ \\t]*x[ \\t]*(\\^" + num + ")?";
        String sin = "[ \\t]*sin[ \\t]*\\([ \\t]*x[ \\t]*\\)" +
                "([ \\t]*\\^" + num + ")?[ \\t]*";
        String cos = "[ \\t]*cos[ \\t]*\\([ \\t]*x[ \\t]*\\)" +
                "([ \\t]*\\^" + num + ")?[ \\t]*";
        String factor =
                "(" + num + ")|(" + pow + ")|(" + sin + ")|(" + cos + ")";
        String product = "[ \\t]*[+-]?[ \\t]*[+-](\\d+[ \\t]*\\*[ \\t]*)?([ " +
                "\\t]*(" + factor + ")[ \\t]*\\*[ \\t]*)*+([ \\t]*"
                + factor + ")[ \\t]*";
        Pattern r = Pattern.compile(product);
        Matcher m = r.matcher(str);
        String cur = new String();
        while (m.find()) {
            cur = cur + m.group();
        }
        if (cur.equals(str)) {
            return true;
        } else {
            return false;
        }
    }
    
    public Poly(ArrayList<Product> p) {
        this.productList = new ArrayList<Product>();
        for (int i = 0; i < p.size(); i++) {
            Product t1 = p.get(i);
            boolean isCombine = false;
            for (int j = 0; j < productList.size(); j++) {
                Product t2 = productList.get(j);
                if (t1.canCombine(t2)) {
                    productList.set(j, t1.Combine(t2));
                    if (productList.get(j).getCoeff().equals(BigInteger.ZERO)) {
                        productList.remove(j);//
                    }
                    isCombine = true;
                    break;
                }
            }
            if (isCombine) {
                continue;
            }
            if (!p.get(i).getCoeff().equals(BigInteger.ZERO)) {
                this.productList.add(p.get(i));
            }
        }
    }
    
    public Poly(String s) throws NumberFormatException {
        productList = new ArrayList<>();
        String str = s; // for checkstyle
        if (str.equals("")) {
            throw new NumberFormatException();
        }
        int pos = -1;
        for (int i = 0;i < str.length(); i++) {
            if (str.charAt(i) != ' ' && str.charAt(i) != '\t') {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            throw new NumberFormatException();
        }
        if (str.charAt(pos) != '+' && str.charAt(pos) != '-') {
            str = "+" + str;
        }
        if (!judgeFormat(str)) {
            throw new NumberFormatException();
        }
        str = str.replaceAll("[ \\t]+", "");
        if (str.equals("")) {
            throw new NumberFormatException();
        }
        while (!str.equals("")) {
            String tmp = matchProduct(str);
            if (tmp.equals("")) {
                throw new NumberFormatException();
            }
            str = str.substring(tmp.length());
            tmp = tmp.replaceAll("(\\+\\+)|(--)", "+");
            tmp = tmp.replaceAll("(\\+-)|(-\\+)", "-");
            Product p = new Product(tmp);
            boolean isCombine = false;
            for (int i = 0; i < productList.size(); i++) {
                Product cur = productList.get(i);
                if (p.canCombine(cur)) {
                    productList.set(i, p.Combine(cur));
                    if (productList.get(i).equals(BigInteger.ZERO)) {
                        productList.remove(i);//
                    }
                    isCombine = true;
                    break;
                }
            }
            if (isCombine) {
                continue;
            }
            if (!p.getCoeff().equals(BigInteger.ZERO)) {
                this.productList.add(p);
            }
        }
    }
    
    public Poly Derivation() {
        ArrayList<Product> p = new ArrayList<Product>();
        for (int i = 0; i < this.productList.size(); i++) {
            p.addAll(this.productList.get(i).Derivation().productList);
        }
        Poly res = new Poly(p);
        return res;
    }
    
    public String ToString() {
        StringBuffer str = new StringBuffer();
        int flag = -1;
        int cnt = 0;
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getCoeff().compareTo(BigInteger.ZERO) > 0) {
                flag = i;
                break;
            }
        }
        if (flag != -1) {
            Product tmp = productList.get(flag);
            str.append(tmp.ToString());
            cnt++;
            productList.remove(flag);
        }
        for (int i = 0; i < productList.size(); i++) { // new size
            Product cur = productList.get(i);
            if (cnt > 0 && cur.getCoeff().compareTo(BigInteger.ZERO) == 1) {
                str.append("+");
            }
            str.append(productList.get(i).ToString());
            cnt++;
        }
        str.trimToSize();
        if (str.length() == 0) {
            str.append("0");
        }
        if (str.charAt(0) == '+') {
            str.deleteCharAt(0);
        }
        return str.toString();
    }
}
