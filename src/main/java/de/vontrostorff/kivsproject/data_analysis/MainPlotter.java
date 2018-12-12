package de.vontrostorff.kivsproject.data_analysis;

import de.vontrostorff.kivsproject.Main;
import de.vontrostorff.kivsproject.data_analysis.plotters.XYAverageLinePlotter;
import de.vontrostorff.kivsproject.data_analysis.plotters.XYScatterOverTimePlotter;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainPlotter {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final PingFile pingFile;
    private final Path outputDir;

    public MainPlotter(PingFile pingFile, Path outputDir) {
        this.pingFile = pingFile;
        this.outputDir = outputDir;
    }

    public void plotAll() {
        List<JFreeChart> plots = new ArrayList<>();
        plots.add(new XYAverageLinePlotter(pingFile).plot());
        plots.add(new XYScatterOverTimePlotter(pingFile).plot());


        plots.forEach(jFreeChart -> {
            int width = 1920;   /* Width of the image */
            int height = 1080;  /* Height of the image */
            Path jpgPath = outputDir.resolve(jFreeChart.getTitle().getText() + "_" + jFreeChart.getID() + ".jpg");
            File jpgFile = jpgPath.toFile();
            try {
                jpgFile.createNewFile();
                ChartUtils.saveChartAsJPEG(jpgFile, jFreeChart, width, height);
            } catch (IOException e) {
                LOGGER.warning("Plot could not be written into file");
                e.printStackTrace();
            }
        });

    }


}
