import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputePoly {
    private ArrayList<Poly> polyList;
    
    public ComputePoly() {
        polyList = new ArrayList<Poly>();
    }
    
    private Matcher myMatch(String s) {
        String pattern = "(\\s*([+-])\\s*([+-])?(\\d+)\\s*" +
                "(\\*\\s*x\\s*(\\^\\s*([+-])?(\\d+))?)?\\s*)" +
                "|(\\s*([+-])\\s*([+-])?\\s*x\\s*(\\^\\s*([+-])?(\\d+))?\\s*)";
        Pattern r = Pattern.compile(pattern);
        return r.matcher(s);
    }
    
    private void parsePoly(String s) {
        String coeff = new String();
        String deg = new String();
        Matcher m = myMatch(s);
        while (m.find()) {
            if (m.group(1) != null) { // 系数存在的情况
                if (m.group(2) == null) { //正数
                    coeff = m.group(4);
                } else if (m.group(2) != null) {
                    if (m.group(3) == null) { //单个符号
                        coeff = m.group(2) + m.group(4);
                    } else if (m.group(2).equals(m.group(3))) { //负负得正
                        coeff = m.group(4);
                    } else { //相反符号
                        coeff = "-" + m.group(4);
                    }
                }
                if (m.group(5) == null) { // 常数项
                    deg = "0";
                } else {
                    if (m.group(6) == null) { // 一次项
                        deg = "1";
                    } else { // 完全项
                        if (m.group(7) == null) {
                            deg = m.group(8);
                        } else {
                            deg = m.group(7) + m.group(8);
                        }
                    }
                }
            } else { // 系数省略的情况
                if (m.group(10) == null) {
                    coeff = "1";
                } else if (m.group(10) != null) {
                    if (m.group(11) == null) { //单个符号
                        coeff = m.group(10) + "1";
                    } else if (m.group(10).equals(m.group(11))) {
                        coeff = "1";
                    } else {
                        coeff = "-" + "1";
                    }
                }
                if (m.group(12) == null) {
                    deg = "1";
                } else {
                    if (m.group(13) == null) {
                        deg = m.group(14);
                    } else {
                        deg = m.group(13) + m.group(14);
                    }
                }
            }
            BigInteger co = new BigInteger(coeff);
            BigInteger de = new BigInteger(deg);
            polyList.add(new Poly(co, de));
        }
    }
    
    private boolean judge(String s) {
        String charSet = "[\\+\\-\\d\\*\\^x\t ]+";
        if (!Pattern.matches(charSet, s)) {
            return true; // true:invalid
        }
        Matcher m = myMatch(s);
        String tmp = new String();
        while (m.find()) {
            tmp = tmp + m.group(0);
        }
        if (!tmp.equals(s)) {
            return true;
        } else {
            return false;
        }
    }
    
    private void combine() {
        for (int i = 0; i < polyList.size(); i++) {
            int flag = 0;
            for (int j = i + 1; j < polyList.size(); j++) {
                if (polyList.get(i).degree().compareTo(
                        polyList.get(j).degree()) == 0) {
                    BigInteger newcoeff = polyList.get(i).coeff().add(
                            polyList.get(j).coeff());
                    BigInteger newdeg = polyList.get(i).degree();
                    polyList.remove(i);
                    polyList.remove(j - 1);
                    Poly newpoly = new Poly(newcoeff, newdeg);
                    polyList.add(newpoly);
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                i = i - 1;
            }
        }
    }
    
    private void work(Poly cur) {
        if (cur.degree().compareTo(BigInteger.ZERO) == 0) {
            if (cur.coeff().abs().compareTo(BigInteger.ONE) == 0) {
                System.out.print(1);
            } else {
                System.out.print(cur.coeff().abs().toString());
            }
        } else if (cur.degree().compareTo(BigInteger.ONE) == 0) {
            if (cur.coeff().abs().compareTo(BigInteger.ONE) == 0) {
                System.out.print("x");
            } else {
                System.out.print(cur.coeff().abs().toString() + "*x");
            }
        } else {
            if (cur.coeff().abs().compareTo(BigInteger.ONE) == 0) {
                System.out.print("x^" + cur.degree().toString());
            } else {
                System.out.print(cur.coeff().abs().toString()
                        + "*x^" + cur.degree().toString());
            }
        }
    }
    
    private void compute() {
        Poly cur;
        int cnt = 0;
        int flag = 0;
        int pos = 0;
        int swap = polyList.size();
        for (int i = 0; i < polyList.size(); i++) {
            cur = polyList.get(i).diff();
            if (cur.coeff().compareTo(BigInteger.ZERO) == 0) {
                continue;
            }
            if (pos == 0 && cur.coeff().compareTo(BigInteger.ZERO) == -1) {
                continue;
            } else if (pos == 0
                    && cur.coeff().compareTo(BigInteger.ZERO) == 1) {
                pos = 1;
                swap = i;
            }
            cnt++;
            flag = 1;
            if (cnt > 1 && cur.coeff().compareTo(BigInteger.ZERO) == -1) {
                System.out.print("-");
            } else if (cnt > 1 && cur.coeff().compareTo(BigInteger.ZERO) == 1) {
                System.out.print("+");
            }
            work(cur);
        }
        for (int i = 0; i < swap; i++) {
            cur = polyList.get(i).diff();
            if (cur.coeff().compareTo(BigInteger.ZERO) == 0) {
                continue;
            }
            cnt++;
            flag = 1;
            if (cur.coeff().compareTo(BigInteger.ZERO) == -1) {
                System.out.print("-");
            } else if (cnt > 1 && cur.coeff().compareTo(BigInteger.ZERO) == 1) {
                System.out.print("+");
            }
            work(cur);
        }
        if (flag == 0) {
            System.out.println("0");
        }
    }
    
    public static void main(String[] args) {
        final ComputePoly cp = new ComputePoly();
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        if (s.equals("")) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        int pos = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ' && s.charAt(i) != '\t') {
                pos = i;
                break;
            }
        } // 空格不影响
        if (s.charAt(pos) != '+' && s.charAt(pos) != '-') {
            s = "+" + s;
        }
        if (cp.judge(s)) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        cp.parsePoly(s);
        cp.combine();
        cp.compute();
    }
}