package ru.liga;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.apache.commons.cli.CommandLine;
import ru.liga.algorithm.Algorithm;
import ru.liga.calculate.Period;
import ru.liga.currency.CurrencyFile;
import ru.liga.service.MyPlot;
import ru.liga.service.ParserInput;
import ru.liga.service.RateService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class InWorkToScatter {
    public static final String PHOTO_PATH = "src/main/resources/graph.png";

    public static String launch(String[] args) throws Exception {
        CommandLine commandLine = ParserInput.parseCommand(args);
        String alg = commandLine.getOptionValue("alg");
        Algorithm algorithm = ParserInput.parseAlgorithm(alg);

        Plot plt = MyPlot.getPlot();

        String result = "";
        String[] currencyArray = ParserInput.parseCurrency(args[1]);

        for (String currency : currencyArray) {
            CurrencyFile currencyString = parseCurrency(currency);
            if (currency == null) {
                return "Невозможно обработать валюту " + currencyString;
            }
            if (commandLine.hasOption("date")) {
                String date = commandLine.getOptionValue("date");
                result = currency + "\n" + RateService.calculateRate(currencyString, LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")), algorithm);
            }
            if (commandLine.hasOption("period")) {
                String periodStr = commandLine.getOptionValue("period");
                Period period = parseCalculateType(periodStr);
                if (commandLine.getOptionValue("output").equalsIgnoreCase("list")) {
                    result += currency + "\n" + RateService.calculateRate(currencyString, period, algorithm) + "\n";
                } else if (commandLine.getOptionValue("output").equalsIgnoreCase("graph")) {
                    String colorCurrency = null;
                    switch (currencyString.name()) {
                        case "USD": colorCurrency = "blue";
                        break;
                        case "EUR": colorCurrency = "red";
                        break;
                        case "TRY": colorCurrency = "orange";
                        break;
                        case "AMD": colorCurrency = "purple";
                        break;
                        case "BGN": colorCurrency = "yellow";
                        break;
                        default: throw new IllegalStateException("Unexpected value: " + currencyString.name());

                    }
                    plt.plot().add(RateService.calculateRateGraph(currencyString, period, algorithm))
                            .color(colorCurrency)
                            .linewidth(1.5)
                            .linestyle("-")
                            .label(currencyString.name());
                }
            }
        }
        if (commandLine.getOptionValue("output").equalsIgnoreCase("graph")) {
            getPhoto(plt);
        } else {
            return result;
        }
        return null;
    }


    public static void getPhoto(Plot plot) throws PythonExecutionException, IOException {
        Plot plt = plot;
        plt.legend();
        plt.savefig(PHOTO_PATH).dpi(200);
        plt.executeSilently();
    }


    /**
     * Определеяет тип валюты и предоставляет строку с адресом к БД.
     *
     * @param s Строка с валютой.
     * @return Адрес к БД.
     */
    private static CurrencyFile parseCurrency(String s) {
        for (CurrencyFile currency : CurrencyFile.values()) {
            if (currency.name().equalsIgnoreCase(s)) {
                return currency;
            }
        }
        return null;
    }

    /**
     * Определеяет период, на который необходимо предоставить информацию.
     *
     * @param s Строка с периодом.
     * @return Период.
     */
    private static Period parseCalculateType(String s) {
        for (Period calculateType : Period.values()) {
            if (calculateType.name().equalsIgnoreCase(s)) {
                return calculateType;
            }
        }
        return null;
    }

}
