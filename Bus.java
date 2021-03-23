package e.user.gumibusremake;

import java.io.Serializable;

public class Bus implements Serializable {
    String busId;
    String busNo;

    public Bus() {

    }

    public Bus(String id) {
        busId = id;
    }


}