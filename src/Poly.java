import java.math.BigInteger;

public class Poly {
    private BigInteger terms; // 系数
    private BigInteger deg; // 指数

    public Poly(BigInteger c, BigInteger n) {
        terms = c;
        deg = n;
    }

    public BigInteger degree() {
        return deg;
    }

    public BigInteger coeff() {
        return terms;
    }

    public Poly diff() {
        if (deg == BigInteger.ZERO) {
            return new Poly(BigInteger.ZERO, BigInteger.ZERO);
        }
        Poly deriv = new Poly(terms.multiply(deg),
                deg.subtract(BigInteger.ONE));
        return deriv;
    }
}