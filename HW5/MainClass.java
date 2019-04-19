import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Dispatcher dis = new Dispatcher();
        InputHandler p1 = new InputHandler(dis);
        Elevator c1 = new Elevator(dis);
        p1.start();
        c1.start();
    }
}