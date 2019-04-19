import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputHandler extends Thread {
    private Dispatcher dispatch;
    
    public InputHandler(Dispatcher dis) {
        dispatch = dis;
    }
    
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            try {
                PersonRequest in = elevatorInput.nextPersonRequest();
                if (in == null) {
                    dispatch.put(new PersonRequestBox(in, true));
                    elevatorInput.close();
                    break;
                } else {
                    dispatch.put(new PersonRequestBox(in, false));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
