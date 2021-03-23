package e.user.gumibusremake;

import java.io.Serializable;

public class BusStop implements Serializable {
    public String stopName;
    public String stopId;
    public String getStopName()
    {
        return stopName;
    }
    public String getstopId() {
        return stopId;
    }
}