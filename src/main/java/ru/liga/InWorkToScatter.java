package ru.liga;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.apache.commons.cli.CommandLine;
import ru.liga.algorithm.Algorithm;
import ru.liga.calculate.Period;
import ru.liga.currency.CurrencyFile;
import ru.liga.service.GraphFormation;
import ru.liga.service.ParserInput;
import ru.liga.service.RateService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.liga.calculate.Period.parseCalculateType;


public class InWorkToScatter {
    public static final String PHOTO_PATH = "src/main/resources/graph.png"; //путь создания и чтения файла (графика)

    public String launch(String[] args) throws Exception {
        CommandLine commandLine = ParserInput.parseCommand(args);
        String alg = commandLine.getOptionValue("alg");
        Algorithm algorithm = ParserInput.parseAlgorithm(alg);
        GraphFormation graphFormation = new GraphFormation();
        Plot plt = graphFormation.getPlot();
        String result = "";

        String[] currencyArray = CurrencyFile.parseCurrency(args[1]);

        RateService rateService = new RateService();
        for (String currency : currencyArray) {
            CurrencyFile currencyString = CurrencyFile.parseCurrencyType(currency);
            if (currency == null) {
                return "Невозможно обработать валюту " + currencyString;
            }
            if (commandLine.hasOption("date")) {
                String date = commandLine.getOptionValue("date");
                result = currency + "\n" + rateService.calculateRate(currencyString, LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")), algorithm);
            }
            if (commandLine.hasOption("period")) {
                String periodStr = commandLine.getOptionValue("period");
                Period period = parseCalculateType(periodStr);
                if (commandLine.getOptionValue("output").equalsIgnoreCase("list")) {
                    result += currency + "\n" + rateService.calculateRate(currencyString, period, algorithm) + "\n";
                } else if (commandLine.getOptionValue("output").equalsIgnoreCase("graph")) {
                    String colorCurrency = graphFormation.graphColor(currencyString);
                    graphFormation.getGraphLine(plt, colorCurrency, currencyString, period, algorithm);
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
    public void getPhoto(Plot plt) throws PythonExecutionException, IOException {
        plt.legend();
        plt.savefig(PHOTO_PATH).dpi(200);
        plt.executeSilently();
    }
}
