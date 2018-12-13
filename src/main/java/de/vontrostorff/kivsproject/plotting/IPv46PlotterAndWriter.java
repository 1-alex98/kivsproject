package de.vontrostorff.kivsproject.plotting;

import de.vontrostorff.kivsproject.Main;
import de.vontrostorff.kivsproject.data_analysis.MainStatisticalWriter;
import de.vontrostorff.kivsproject.parsing.dtos.Ping;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;
import de.vontrostorff.kivsproject.plotting.plotters.RTTBoxAndWhiskerPlotterIPV46;
import de.vontrostorff.kivsproject.plotting.plotters.XYScatterOverTimePlotterIPv46;
import de.vontrostorff.kivsproject.plotting.plotters.XYScatterUnsuccessfulPingsIPV46;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class IPv46PlotterAndWriter {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final PingFile originalPingFile;
    private final Path outputDir;

    public IPv46PlotterAndWriter(PingFile originalPingFile, Path outputDir) {
        this.originalPingFile = originalPingFile;
        this.outputDir = outputDir;
    }

    public void plotAndWrite() throws IOException {
        List[] pingGroupLists = dividePingFile();
        List<PingGroup> ipv4 = pingGroupLists[0];
        List<PingGroup> ipv6 = pingGroupLists[1];

        List<JFreeChart> plots = new ArrayList<>();
        plots.add(new XYScatterOverTimePlotterIPv46(ipv4,ipv6).plot());
        plots.add(new XYScatterUnsuccessfulPingsIPV46(ipv4, ipv6).plot());
        plots.add(new RTTBoxAndWhiskerPlotterIPV46(ipv4, ipv6).plot());

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

        List<Float> ipv4PingRTTs = ipv4.stream()
                .flatMap(pingGroup -> pingGroup.getPings().stream())
                .map(Ping::getRoundTripTime)
                .collect(Collectors.toList());
        new MainStatisticalWriter(outputDir.resolve("ipv4-stats.txt"), ipv4PingRTTs, ipv4).writeFile();

        List<Float> ipv6PingRTTs = ipv6.stream()
                .flatMap(pingGroup -> pingGroup.getPings().stream())
                .map(Ping::getRoundTripTime)
                .collect(Collectors.toList());
        new MainStatisticalWriter(outputDir.resolve("ipv6-stats.txt"), ipv6PingRTTs, ipv6).writeFile();

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
