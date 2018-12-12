package de.vontrostorff.kivsproject.parsing.dtos;


import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PingFile {
    private final List<PingGroup> pingGroups= new ArrayList<>();
    private OffsetDateTime downloadStart;
}
