package de.vontrostorff.kivsproject.parsing;

import de.vontrostorff.kivsproject.parsing.dtos.Ping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.vontrostorff.kivsproject.util.Util.isNullOrEmpty;

public class PingParser {
    private static final Pattern PING_PATTERN= Pattern.compile("(?<bytes>\\d{1,3}) bytes from (?<source>[^:]*):[^=]*=(?<sequencenumber>[\\d]{1,2}).*time=(?<time>\\d{1,2}.\\d{0,5}).*");
    private final String toParse;
    public PingParser(String toParse) {
        this.toParse = toParse;
    }

    public Ping parse() {
        Ping ping= new Ping();
        Matcher matcher = PING_PATTERN.matcher(toParse);
        if(!matcher.matches()){
            throw new IllegalStateException("The ping was in invalid format");
        }
        String bytes = matcher.group("bytes");
        if(isNullOrEmpty(bytes)){
            throw new IllegalStateException("The ping had to bytes");
        }
        int bytesInt= Integer.parseInt(bytes);
        ping.setBytes(bytesInt);

        String source = matcher.group("source");
        if(isNullOrEmpty(source)){
            throw new IllegalStateException("The ping had to source");
        }
        ping.setSource(source);

        String sequenceNumber = matcher.group("sequencenumber");
        if(isNullOrEmpty(sequenceNumber)){
            throw new IllegalStateException("The ping had to sequenceNumber");
        }
        int sequenceNumberInt= Integer.parseInt(sequenceNumber);
        ping.setSequence(sequenceNumberInt);

        String time = matcher.group("time");
        if(isNullOrEmpty(time)){
            throw new IllegalStateException("The ping had to time");
        }
        float timeFloat= Float.parseFloat(time);
        ping.setRoundTripTime(timeFloat);

        return ping;
    }
}
