package ru.liga.service;

import com.github.sh0nk.matplotlib4j.Plot;

public class MyPlot {

    public static Plot getPlot() {
        Plot plt = Plot.create();
        plt.title("Exchange Rate");
        plt.ylabel("Rate");
        plt.xlabel("Days");
        return plt;
    }
}
