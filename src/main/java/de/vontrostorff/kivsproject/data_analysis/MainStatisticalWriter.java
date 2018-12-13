package de.vontrostorff.kivsproject.data_analysis;

import de.vontrostorff.kivsproject.Main;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

public class MainStatisticalWriter {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final Path outPutFile;
    private final List<Float> pingRttStream;
    private final List<PingGroup> pingGroups;

    public MainStatisticalWriter(Path outPutFile, List<Float> pingRttStream, List<PingGroup> pingGroups) {
        this.outPutFile = outPutFile;
        this.pingRttStream = pingRttStream;
        this.pingGroups = pingGroups;
    }

    public void writeFile() throws IOException {
        outPutFile.toFile().createNewFile();
        FileWriter fileWriter = new FileWriter(outPutFile.toFile());
        fileWriter.write(MessageFormat.format("Overall average of successful pings ''{0}'' ms", getOverallAverage()));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(MessageFormat.format("Overall min of successful pings ''{0}'' ms", getOverallMin()));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(MessageFormat.format("Overall max of successful pings ''{0}'' ms", getOverallMax()));
        fileWriter.write(System.getProperty("line.separator"));
        Double variance = getVariance();
        fileWriter.write(MessageFormat.format("Overall standard deviation of successful pings ''{0}'' ms", variance));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(MessageFormat.format("Overall variance of successful pings ''{0}'' ms", Math.sqrt(variance)));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(MessageFormat.format("Median of successful pings ''{0}'' ms", getMedian()));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(MessageFormat.format("Unsuccessful pings ''{0}''", getUnsuccessfulPings()));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(MessageFormat.format("Sent pings ''{0}''", getSentPings()));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(MessageFormat.format("Failed pings percentage pings ''{0}'' %", (((float) getUnsuccessfulPings()) / getSentPings()) * 100));
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.flush();
        fileWriter.close();
        //TODO: more measures
    }

    private int getSentPings() {
        return pingGroups.stream()
                .mapToInt(PingGroup::getTransmitted)
                .sum();
    }

    private int getUnsuccessfulPings() {
        return pingGroups.stream()
                .mapToInt(value -> value.getTransmitted() - value.getReceived())
                .sum();
    }

    private Double getOverallAverage() {
        return pingRttStream
                .stream()
                .mapToDouble(value -> value)
                .average()
                .getAsDouble();
    }

    private Float getOverallMax() {
        return pingRttStream
                .stream()
                .max(Float::compareTo)
                .get();
    }

    private Float getOverallMin() {
        return pingRttStream
                .stream()
                .min(Float::compareTo)
                .get();
    }


    private Double getVariance() {
        Double overallAverage = getOverallAverage();
        return pingRttStream
                .stream()
                .mapToDouble(value -> value)
                .map(operand -> Math.pow(operand - overallAverage, 2))
                .sum() / pingRttStream.size();
    }


    private Float getMedian() {
        pingRttStream.sort(Float::compareTo);
        return pingRttStream.get((int) Math.ceil(((pingRttStream.size() - 1) / 2d)));
    }
}
