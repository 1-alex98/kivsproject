package de.vontrostorff.kivsproject.plotting.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.util.JFreeChartUtil;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYScatterOverTimePlotter implements Plotter {
    private final PingFile pingFile;

    public XYScatterOverTimePlotter(PingFile pingFile) {
        this.pingFile = pingFile;
    }

    @Override
    public JFreeChart plot() {
        XYSeries rttSeries = new XYSeries("RTT");
        pingFile.getPingGroups()
                .forEach(pingGroup -> pingGroup.getPings().forEach(ping -> rttSeries.add(pingGroup.getStart().getTime(), ping.getRoundTripTime())));

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(rttSeries);

        JFreeChart xylineChart = JFreeChartUtil.getScatterOverTime(
                "rtt for every ping over time",
                "Time",
                "RTT in seconds",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        xylineChart.setID("2");
        return xylineChart;
    }
}
