package de.vontrostorff.kivsproject.parsing;

import de.vontrostorff.kivsproject.parsing.dtos.Ping;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;

import java.io.BufferedReader;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.vontrostorff.kivsproject.util.Util.isNullOrEmpty;

public class PingGroupParser {
    private static final Logger LOGGER = Logger.getLogger(PingGroupParser.class.getName());
    private static final String SEPERATOR_IPV4="round-trip min";
    private static final String SEPERATOR_IPV6="rtt min";
    private static final Pattern PING_GROUP_PATTER= Pattern.compile("(?<unixtime>[\\d]{1,20})\\nPING www.google.de[^\\n]+\\n(?<ping>[\\d]{2,3} bytes from[^\\n]*\\n)*\\n[^\\n]+\\n(?<transmitted>\\d{1,3}) packets transmitted, (?<received>\\d{1,3})[^\\n]*\\n[^=]*[=] (?<min>[\\d]{1,4}[.][\\d]{1,4})\\/(?<average>[\\d]{1,4}[.][\\d]{1,4})\\/(?<max>[\\d]{1,5}[.][\\d]{1,5})[^\\n]*\\n");


    private final BufferedReader bufferedReader;
    public PingGroupParser(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public PingGroup parseNextPingGroup() throws Exception {
        PingGroup pingGroup= new PingGroup();
        LOGGER.log(Level.FINE,"Starting to parse group");
        StringBuilder stringBuilder= new StringBuilder();
        String nextLine;
        boolean lastLine;
        do{
            nextLine= bufferedReader.readLine();
            stringBuilder.append(nextLine);
            stringBuilder.append('\n');
            if(nextLine==null){
                return null;
            }
            lastLine= nextLine.contains(SEPERATOR_IPV4) || nextLine.contains(SEPERATOR_IPV6);
        }while(!lastLine);
        Matcher matcher = PING_GROUP_PATTER.matcher(stringBuilder.toString());
        if(!matcher.find()){
            throw new IllegalStateException("The following ping group did not match: "+stringBuilder.toString());
        }
        String unixtime = matcher.group("unixtime");
        if(isNullOrEmpty(unixtime)){
            throw new IllegalStateException("The following ping group had no unixtime: "+stringBuilder.toString());
        }
        int unixTimeAsInt= Integer.parseInt(unixtime);
        pingGroup.setStart(new Date(unixTimeAsInt*1000L));
        String transmitted = matcher.group("transmitted");
        if(isNullOrEmpty(transmitted)){
            throw new IllegalStateException("The following ping group had no transmitted: "+stringBuilder.toString());
        }
        int transmittedInt= Integer.parseInt(transmitted);
        pingGroup.setTransmitted(transmittedInt);
        String received = matcher.group("received");
        if(isNullOrEmpty(received)){
            throw new IllegalStateException("The following ping group had no received: "+stringBuilder.toString());
        }
        int receivedInt= Integer.parseInt(received);
        pingGroup.setReceived(receivedInt);
        String min = matcher.group("min");
        if(isNullOrEmpty(min)){
            throw new IllegalStateException("The following ping group had no min: "+stringBuilder.toString());
        }
        float minFloat= Float.parseFloat(min);
        pingGroup.setMin(minFloat);
        String average = matcher.group("average");
        if(isNullOrEmpty(average)){
            throw new IllegalStateException("The following ping group had no average: "+stringBuilder.toString());
        }
        float averageInt= Float.parseFloat(average);
        pingGroup.setAverage(averageInt);
        String max = matcher.group("max");
        if(isNullOrEmpty(max)){
            throw new IllegalStateException("The following ping group had no max: "+stringBuilder.toString());
        }
        float maxInt= Float.parseFloat(max);
        pingGroup.setAverage(maxInt);
        String ping = matcher.group("ping");
        String[] split = ping.split(System.getProperty("line.separator"));
        List<Ping> pings = Arrays.stream(split).map(s -> {
            try{
                return new PingParser(s).parse();
            }catch (Exception e){
                LOGGER.warning("The following ping was invalid: "+s);
                e.printStackTrace();
                return new Ping();
            }
        }).filter(ping1 -> ping1.getSequence()!=null)
        .collect(Collectors.toList());
        pingGroup.getPings().addAll(pings);
        return pingGroup;
    }
}
