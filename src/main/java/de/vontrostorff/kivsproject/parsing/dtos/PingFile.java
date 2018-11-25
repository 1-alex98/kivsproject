package de.vontrostorff.kivsproject.parsing.dtos;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PingFile {
    private final List<PingGroup> pingGroups= new ArrayList<>();
    private OffsetDateTime downloadStart;

    public PingFile() {
        downloadStart= OffsetDateTime.now();
    }

    public List<PingGroup> getPingGroups() {
        return pingGroups;
    }

    public OffsetDateTime getDownloadStart() {
        return downloadStart;
    }

    public void setDownloadStart(OffsetDateTime downloadStart) {
        this.downloadStart = downloadStart;
    }
}
