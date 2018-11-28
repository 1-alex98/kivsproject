package de.vontrostorff.kivsproject.parsing.dtos;


import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PingGroup {
    private final List<Ping> pings= new ArrayList<>(11);
    private float min;
    private float max;
    private float average;
    private Date start;
    private int transmitted;
    private int received;

}
