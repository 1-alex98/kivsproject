package de.vontrostorff.kivsproject.parsing.dtos;


public class Ping {
    private Integer sequence;
    private int bytes;
    private float roundTripTime;
    private String source;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public float getRoundTripTime() {
        return roundTripTime;
    }

    public void setRoundTripTime(float roundTripTime) {
        this.roundTripTime = roundTripTime;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
