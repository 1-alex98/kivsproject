package de.vontrostorff.kivsproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.OptionalDouble;
import java.util.logging.Logger;

import de.vontrostorff.kivsproject.download.FileDownloader;
import de.vontrostorff.kivsproject.parsing.FileParser;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        URL url = new URL(args[0]);
        BufferedReader readerForFile = FileDownloader.getReaderForFile(url);
        FileParser fileParser = new FileParser(readerForFile);
        PingFile pingFile = fileParser.parse();
        LOGGER.info("A file with "+pingFile.getPingGroups().size()+" ping groups was parsed");

        OptionalDouble average = pingFile.getPingGroups().stream().mapToDouble(PingGroup::getAverage).average();
        double asDouble = average.getAsDouble();
        System.out.println(asDouble);
    }
}
