import java.util.ArrayList;

public class Dispatcher {
    private ArrayList<PersonRequestBox> requestList;
    
    public Dispatcher() {
        requestList = new ArrayList<>();
    }
    
    public synchronized boolean isEmpty() {
        return requestList.isEmpty();
    }

    public synchronized PersonRequestBox get() {
        while (requestList.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                //DO STH
            }
        }
        PersonRequestBox q = requestList.remove(0);
        notifyAll();
        return q;
    }
    
    public synchronized void put(PersonRequestBox q) {
        while (requestList.size() >= 30) { // limit==30
            try {
                wait();
            } catch (InterruptedException e) {
                //DO STH
            }
        }
        requestList.add(q);
        notifyAll();
    }
}
