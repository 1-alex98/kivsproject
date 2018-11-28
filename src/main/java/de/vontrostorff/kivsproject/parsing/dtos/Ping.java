package de.vontrostorff.kivsproject.parsing.dtos;

import lombok.Data;

@Data
public class Ping {
    private Integer sequence;
    private int bytes;
    private float roundTripTime;
    private String source;
}
