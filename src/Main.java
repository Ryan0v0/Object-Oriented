import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            String str = in.nextLine();
            Poly poly = new Poly(str);
            Product res = poly.Derivation();
            System.out.println(res.toString());
            //System.out.println(poly.Derivation().toString());
        } catch (NoSuchFieldError | NoSuchElementException
                | NumberFormatException e) {
            System.out.println("WRONG FORMAT!");
        }
    }
}
