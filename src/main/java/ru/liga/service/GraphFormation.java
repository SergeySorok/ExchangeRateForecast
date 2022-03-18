package ru.liga.service;

import com.github.sh0nk.matplotlib4j.Plot;
import ru.liga.algorithm.Algorithm;
import ru.liga.calculate.Period;
import ru.liga.currency.CurrencyFile;

import static ru.liga.currency.CurrencyFile.USD;

public class GraphFormation {

    public Plot getPlot() {
        Plot plt = Plot.create();
        plt.title("Exchange Rate");
        plt.ylabel("Rate");
        plt.xlabel("Days");
        return plt;
    }

    public String graphColor (CurrencyFile currencyFile) {
        String colorCurrency = "";
        try {
        switch (currencyFile.name()) {
            case "USD":
                colorCurrency = "blue";
                break;
            case "EUR":
                colorCurrency = "red";
                break;
            case "TRY":
                colorCurrency = "orange";
                break;
            case "AMD":
                colorCurrency = "purple";
                break;
            case "BGN":
                colorCurrency = "yellow";
                break;
        }
       //     default: throw new IllegalStateException("Unexpected value: " + currencyString.name());

        } catch (Exception e) {
            e.getMessage();
        }
        return colorCurrency;
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
