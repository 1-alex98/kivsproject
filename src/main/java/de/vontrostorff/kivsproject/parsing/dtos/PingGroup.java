package de.vontrostorff.kivsproject.parsing.dtos;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PingGroup {
    private final List<Ping> pings= new ArrayList<>(11);
    private float min;
    private float max;
    private float average;
    private Date start;
    private int transmitted;
    private int received;

    public void setMin(float min) {
        this.min = min;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public List<Ping> getPings() {
        return pings;
    }

    public int getTransmitted() {
        return transmitted;
    }

    public void setTransmitted(int transmitted) {
        this.transmitted = transmitted;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getAverage() {
        return average;
    }

    public Date getStart() {
        return start;
    }
}
