package de.vontrostorff.kivsproject.plotting.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.Ping;
import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.util.JFreeChartUtil;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;
import java.util.stream.Collectors;

public class XYScatterOverPingNumberPlotter implements Plotter {
    private final PingFile pingFile;

    public XYScatterOverPingNumberPlotter(PingFile pingFile) {
        this.pingFile = pingFile;
    }

    @Override
    public JFreeChart plot() {
        XYSeries rttSeries = new XYSeries("RTT over sequence number");
        List<Ping> collect = pingFile.getPingGroups().stream()
                .flatMap(pingGroup -> pingGroup.getPings().stream())
                .collect(Collectors.toList());
        int i = 0;
        for (Ping ping : collect) {
            rttSeries.add(i++, ping.getRoundTripTime());
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(rttSeries);

        JFreeChart xylineChart = JFreeChartUtil.getScatterOverInteger(
                "rtt for pings sorted by number",
                "Ping number",
                "RTT in seconds",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        xylineChart.setID("7");
        return xylineChart;
    }
}
