import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            String str = in.nextLine();
            Poly poly = new Poly(str);
            //poly = poly.Derivation();
            //System.out.println(poly.ToString());
            System.out.println(poly.Derivation().ToString());
        } catch (NoSuchFieldError | NoSuchElementException
                | NumberFormatException e) {
            System.out.println("WRONG FORMAT!");
        }
    }
}
