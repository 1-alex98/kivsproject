package de.vontrostorff.kivsproject.data_analysis.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class XYAverageLinePlotter implements Plotter {
    private final PingFile pingFile;

    public XYAverageLinePlotter(PingFile pingFile) {
        this.pingFile = pingFile;
    }

    @Override
    public JFreeChart plot() {
        TimeSeries rttSeries = new TimeSeries("RTT");
        pingFile.getPingGroups()
                .forEach(pingGroup -> rttSeries.add(new FixedMillisecond(pingGroup.getStart()), pingGroup.getAverage()));

        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(rttSeries);

        JFreeChart xylineChart = ChartFactory.createXYStepChart(
                "Average rtt over time",
                "Time",
                "RTT in seconds",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        xylineChart.setID("1");
        return xylineChart;
    }
}
