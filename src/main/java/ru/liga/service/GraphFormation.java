package ru.liga.service;

import com.github.sh0nk.matplotlib4j.Plot;
import ru.liga.algorithm.Algorithm;
import ru.liga.calculate.Period;
import ru.liga.currency.CurrencyFile;

import java.io.IOException;

public class GraphFormation {
    private static final String BLUE_COLOR_GRAPH_LINE = "blue";
    private static final String RED_COLOR_GRAPH_LINE = "red";
    private static final String ORANGE_COLOR_GRAPH_LINE = "orange";
    private static final String PURPLE_COLOR_GRAPH_LINE = "purple";
    private static final String YELLOW_COLOR_GRAPH_LINE = "yellow";
    private static final double LINE_WIDTH_GRAPH = 1.5;
    private static final String LINE_STYLE_GRAPH = "-";

    public Plot getPlot() {
        Plot plt = Plot.create();
        plt.title("Exchange Rate");
        plt.ylabel("Rate");
        plt.xlabel("Days");
        return plt;
    }

    public String graphColor(CurrencyFile currencyFile) {
        switch (currencyFile.name()) {
            case "USD": return BLUE_COLOR_GRAPH_LINE;
            case "EUR": return RED_COLOR_GRAPH_LINE;
            case "TRY": return ORANGE_COLOR_GRAPH_LINE;
            case "AMD": return PURPLE_COLOR_GRAPH_LINE;
            case "BGN": return YELLOW_COLOR_GRAPH_LINE;
            default: throw new IllegalArgumentException("No such currencyColor: [%s]" + currencyFile);
        }
    }

    public Plot getGraphLine(Plot plt, String colorCurrency, CurrencyFile currencyFile, Period period, Algorithm algorithm) throws IOException {
        RateService rateService = new RateService();
        plt.plot().add(rateService.calculateRateGraph(currencyFile, period, algorithm))
                .color(colorCurrency)
                .linewidth(LINE_WIDTH_GRAPH)
                .linestyle(LINE_STYLE_GRAPH)
                .label(currencyFile.name());
        return plt;
    }
}
