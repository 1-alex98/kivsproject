package de.vontrostorff.kivsproject.plotting.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;
import de.vontrostorff.kivsproject.util.JFreeChartUtil;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;

public class XYScatterUnsuccessfulPingsIPV46 implements Plotter {
    private final List<PingGroup> ip4;
    private final List<PingGroup> ip6;

    public XYScatterUnsuccessfulPingsIPV46(List<PingGroup> ip4, List<PingGroup> ip6) {
        this.ip4 = ip4;
        this.ip6 = ip6;
    }

    @Override
    public JFreeChart plot() {
        XYSeries ip4Series = new XYSeries("ipv4");
        ip4
                .forEach(pingGroup -> ip4Series.add(pingGroup.getStart().getTime(), (pingGroup.getTransmitted() - pingGroup.getReceived())));

        XYSeries ip6Series = new XYSeries("ipv6");
        ip6
                .forEach(pingGroup -> ip6Series.add(pingGroup.getStart().getTime(), (pingGroup.getTransmitted() - pingGroup.getReceived())));

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(ip4Series);
        dataset.addSeries(ip6Series);

        JFreeChart xylineChart = JFreeChartUtil.getScatterOverIntegerYAxisAndDateForX(
                "Number of failed ping per command over time",
                "Time",
                "number of failed pings",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        xylineChart.setID("5");
        xylineChart.getXYPlot().getRangeAxis().setRange(0.0, 5.0);
        return xylineChart;
    }
}
