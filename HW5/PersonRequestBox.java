import com.oocourse.elevator1.PersonRequest;

public class PersonRequestBox {
    private PersonRequest request;
    private boolean stop;
    
    public PersonRequestBox(PersonRequest req, boolean st) {
        request = req;
        stop = st;
    }
    
    public PersonRequest getRequest() {
        return request;
    }
    
    public boolean isStop() {
        return stop;
    }
}
