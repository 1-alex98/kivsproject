package de.vontrostorff.kivsproject.plotting;

import de.vontrostorff.kivsproject.Main;
import de.vontrostorff.kivsproject.parsing.dtos.Ping;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;
import de.vontrostorff.kivsproject.plotting.plotters.XYScatterOverTimePlotterIPv46;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class IPv46Plotter {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final PingFile originalPingFile;
    private final Path outputDir;

    public IPv46Plotter(PingFile originalPingFile, Path outputDir) {
        this.originalPingFile = originalPingFile;
        this.outputDir = outputDir;
    }

    public void plot(){
        List[] pingGroupLists = dividePingFile();
        List ipv4 = pingGroupLists[0];
        List ipv6= pingGroupLists[1];

        List<JFreeChart> plots = new ArrayList<>();
        plots.add(new XYScatterOverTimePlotterIPv46(ipv4,ipv6).plot());

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

    private List[] dividePingFile() {
        List<PingGroup> ip4PingGroups=new ArrayList<>();
        List<PingGroup> ip6PingGroups=new ArrayList<>();
        for (PingGroup pingGroup : originalPingFile.getPingGroups()) {
            if(pingGroup.getPings().isEmpty()) continue;
            Ping ping = pingGroup.getPings().get(0);
            if(ping.getSource().contains(":")) {
                ip6PingGroups.add(pingGroup);
            }else{
                ip4PingGroups.add(pingGroup);
            }

        }
        return new List[]{ip4PingGroups, ip6PingGroups};
    }
}
