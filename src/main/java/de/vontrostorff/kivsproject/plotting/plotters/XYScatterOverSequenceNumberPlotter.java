package de.vontrostorff.kivsproject.plotting.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.PingFile;
import de.vontrostorff.kivsproject.util.JFreeChartUtil;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYScatterOverSequenceNumberPlotter implements Plotter {
    private final PingFile pingFile;

    public XYScatterOverSequenceNumberPlotter(PingFile pingFile) {
        this.pingFile = pingFile;
    }

    @Override
    public JFreeChart plot() {
        XYSeries rttSeries = new XYSeries("RTT over sequence number");
        pingFile.getPingGroups()
                .forEach(pingGroup -> pingGroup.getPings().forEach(ping -> rttSeries.add(ping.getSequence(), (Number) ping.getRoundTripTime())));

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(rttSeries);

        JFreeChart xylineChart = JFreeChartUtil.getScatterOverInteger(
                "rtt for sequence number",
                "Sequence number",
                "RTT in seconds",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        xylineChart.setID("3");
        xylineChart.getXYPlot().getDomainAxis().setRange(1.0, 10.0);
        return xylineChart;
    }
}
