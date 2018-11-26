package de.vontrostorff.kivsproject.plotting.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYAverageLinePlotter implements Plotter {
    private final PingFile pingFile;

    public XYAverageLinePlotter(PingFile pingFile) {
        this.pingFile = pingFile;
    }

    @Override
    public JFreeChart plot() {

        XYSeries rttSeries = new XYSeries("RTT");
        pingFile.getPingGroups()
                .forEach(pingGroup -> rttSeries.add(pingGroup.getStart().getTime(), pingGroup.getAverage()));

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(rttSeries);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Average rtt over time",
                "Time",
                "RTT in seconds",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        return xylineChart;
    }
}
