import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class Elevator extends Thread {
    private Dispatcher dispatch;
    private int last;
    
    public Elevator(Dispatcher dis) {
        dispatch = dis;
        last = 1;
    }
    
    private void output(int last, int id, int from, int to)
            throws InterruptedException {
        Thread.sleep(Math.abs(from - last) * 500);
        TimableOutput.println(String.format("OPEN-%d", from));
        Thread.sleep(250);
        TimableOutput.println(String.format("IN-%d-%d", id, from));
        Thread.sleep(250);
        TimableOutput.println(String.format("CLOSE-%d", from));
        
        Thread.sleep(Math.abs(from - to) * 500);
        TimableOutput.println(String.format("OPEN-%d", to));
        Thread.sleep(250);
        TimableOutput.println(String.format("OUT-%d-%d", id, to));
        Thread.sleep(250);
        TimableOutput.println(String.format("CLOSE-%d", to));
    }
    
    public void run() {
        //System.out.println("----------CONSUMER---------");
        PersonRequestBox q;
        last = 1;
        while (true) {
            try {
                q = dispatch.get();
                if (q.isStop() && dispatch.isEmpty()) {
                    //System.out.println("----------乌拉(*/ω＼*)---------");
                    break;
                }
                if (q.isStop()) {
                    continue;
                }
                PersonRequest r = q.getRequest();
                int id = r.getPersonId();
                int from = r.getFromFloor();
                int to = r.getToFloor();
                output(last, r.getPersonId(), r.getFromFloor(), r.getToFloor());
                last = r.getToFloor();
            } catch (InterruptedException e) {
                //DO SOMETHING..
            }
        }
        //System.out.println("----------CONSUMER-END---------");
    }
}
