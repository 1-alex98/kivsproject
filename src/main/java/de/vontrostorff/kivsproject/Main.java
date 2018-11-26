package de.vontrostorff.kivsproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.OptionalDouble;
import java.util.logging.Logger;

import de.vontrostorff.kivsproject.download.FileDownloader;
import de.vontrostorff.kivsproject.parsing.FileParser;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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

        XYSeries rttSeries = new XYSeries("RTT");
        pingFile.getPingGroups()
                .forEach(pingGroup -> rttSeries.add(pingGroup.getStart().getTime(),pingGroup.getAverage()));

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries( rttSeries );

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Average rtt over time",
                "Time",
                "RTT in seconds",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */
        File XYChart = new File( "C:\\Users\\alexa\\Desktop\\XYLineChart.jpeg" );
        XYChart.createNewFile();
        ChartUtils.saveChartAsJPEG( XYChart, xylineChart, width, height);
    }
}
