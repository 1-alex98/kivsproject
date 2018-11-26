package de.vontrostorff.kivsproject;

import de.vontrostorff.kivsproject.download.FileDownloader;
import de.vontrostorff.kivsproject.parsing.FileParser;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;
import de.vontrostorff.kivsproject.plotting.MainPlotter;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.OptionalDouble;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        URL url = new URL(args[0]);
        BufferedReader readerForFile = FileDownloader.getReaderForFile(url);
        FileParser fileParser = new FileParser(readerForFile);
        PingFile pingFile = fileParser.parse();
        LOGGER.info("A file with " + pingFile.getPingGroups().size() + " ping groups were parsed");

        OptionalDouble average = pingFile.getPingGroups().stream().mapToDouble(PingGroup::getAverage).average();
        double asDouble = average.getAsDouble();
        System.out.println(asDouble);

        Path outputDir = Paths.get(args[1]);
        outputDir.toFile().mkdirs();
        MainPlotter mainPlotter = new MainPlotter(pingFile, outputDir);
        mainPlotter.plotAll();

    }
}
