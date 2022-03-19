package ru.liga.service;

import com.github.sh0nk.matplotlib4j.Plot;
import ru.liga.algorithm.Algorithm;
import ru.liga.calculate.Period;
import ru.liga.currency.CurrencyFile;

public class GraphFormation {

    public Plot getPlot() {
        Plot plt = Plot.create();
        plt.title("Exchange Rate");
        plt.ylabel("Rate");
        plt.xlabel("Days");
        return plt;
    }

    public String graphColor(CurrencyFile currencyFile) {
        switch (currencyFile.name()) {
            case "USD": return "blue";
            case "EUR": return "red";
            case "TRY": return "orange";
            case "AMD": return "purple";
            case "BGN": return "yellow";
            default: throw new IllegalArgumentException("No such currencyColor: [%s]" + currencyFile);
        }
    }

    public Plot getGraphLine(Plot plt, String colorCurrency, CurrencyFile currencyFile, Period period, Algorithm algorithm) {
        RateService rateService = new RateService();
        plt.plot().add(rateService.calculateRateGraph(currencyFile, period, algorithm))
                .color(colorCurrency)
                .linewidth(1.5)
                .linestyle("-")
                .label(currencyFile.name());
        return plt;
    }
}
