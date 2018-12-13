package de.vontrostorff.kivsproject.plotting.plotters;

import de.vontrostorff.kivsproject.parsing.dtos.Ping;
import de.vontrostorff.kivsproject.parsing.dtos.PingGroup;
import de.vontrostorff.kivsproject.util.JFreeChartUtil;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.util.List;
import java.util.stream.Collectors;

public class RTTBoxAndWhiskerPlotterIPV46 implements Plotter {
    private final List<PingGroup> ip4;
    private final List<PingGroup> ip6;

    public RTTBoxAndWhiskerPlotterIPV46(List<PingGroup> ip4, List<PingGroup> ip6) {
        this.ip4 = ip4;
        this.ip6 = ip6;
    }

    @Override
    public JFreeChart plot() {
        final BoxAndWhiskerCategoryDataset dataset = computeData();

        JFreeChart chart = JFreeChartUtil.custonBoxPlot(
                "Box and Whisker Chart RTT", "IP version", "RTT in ms", dataset, true);
        chart.setID("6");
        return chart;
    }

    private BoxAndWhiskerCategoryDataset computeData() {
        final DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();

        List<Float> ipv4List = ip4.stream()
                .flatMap(value -> value.getPings().stream())
                .map(Ping::getRoundTripTime)
                .collect(Collectors.toList());
        dataset.add(ipv4List, "ipv4", "RTT");

        List<Float> ipv6List = ip6.stream()
                .flatMap(value -> value.getPings().stream())
                .map(Ping::getRoundTripTime)
                .collect(Collectors.toList());
        dataset.add(ipv6List, "ipv6", "RTT");

        return dataset;
    }
}
