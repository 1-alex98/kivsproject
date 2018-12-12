package de.vontrostorff.kivsproject.data_analysis;

import de.vontrostorff.kivsproject.Main;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

public class MainStatisticalWriter {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final PingFile pingFile;
    private final Path outputDir;
    private final List<Float> pingRttStream;

    public MainStatisticalWriter(PingFile pingFile, Path outputDir, List<Float> pingRttStream) {
        this.pingFile = pingFile;
        this.outputDir = outputDir;
        this.pingRttStream = pingRttStream;
    }

    public void writeFile() throws IOException {
        Path outputFile = outputDir.resolve("stats.txt");
        outputDir.toFile().mkdirs();
        outputFile.toFile().createNewFile();
        FileWriter fileWriter = new FileWriter(outputFile.toFile());
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
        fileWriter.flush();
        fileWriter.close();
        //TODO: more measures
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
