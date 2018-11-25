package de.vontrostorff.kivsproject.parsing;

import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;

import java.io.BufferedReader;
import java.util.logging.Logger;

public class FileParser {
    private static final Logger LOGGER = Logger.getLogger(FileParser.class.getName());
    private final BufferedReader bufferedReader;

    public FileParser(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public PingFile parse(){
        PingFile pingFile = new PingFile();
        PingGroup nextPingGroup=null;
        do{
            try {
                nextPingGroup = new PingGroupParser(bufferedReader).parseNextPingGroup();
            }catch (Exception e){
                LOGGER.warning("PingGroup has been thrown away");
                e.printStackTrace();
                continue;
            }
            if(nextPingGroup!=null)pingFile.getPingGroups().add(nextPingGroup);

        }while(nextPingGroup!=null);
        return pingFile;
    }
}
