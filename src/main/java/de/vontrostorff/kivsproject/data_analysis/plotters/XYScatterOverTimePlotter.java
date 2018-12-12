package de.vontrostorff.kivsproject.data_analysis.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.util.JFreeChartUtil;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class XYScatterOverTimePlotter implements Plotter {
    private final PingFile pingFile;

    public XYScatterOverTimePlotter(PingFile pingFile) {
        this.pingFile = pingFile;
    }

    @Override
    public JFreeChart plot() {
        TimeSeries rttSeries = new TimeSeries("RTT");
        pingFile.getPingGroups()
                .forEach(pingGroup -> pingGroup.getPings().forEach(ping -> rttSeries.add(new FixedMillisecond(pingGroup.getStart()), ping.getRoundTripTime())));

        final TimeSeriesCollection dataset = new TimeSeriesCollection();
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
