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
import static ru.liga.service.ParserInput.parseCurrenciesIndex;


public class CommandProcessingLogic {
    public static final String PHOTO_PATH = "photo_file/graph.png"; //путь создания и чтения файла (графика)
    private static final String SPLIT_SEPARATOR = " ";
    private static final String LINE_BREAK = "\n";
    private static final int PHOTO_SIZE = 200;
    private static final String DATE_COMMAND = "date";
    private static final String PERIOD_COMMAND = "period";
    private static final String OUTPUT_COMMAND = "output";
    private static final String OUTPUT_COMMAND_GRAPH = "graph";
    private static final String OUTPUT_COMMAND_LIST = "list";
    private static final String ALGORITHM_COMMAND = "alg";
    private static final String ERROR_CURRENCY_MESSAGE = "Невозможно обработать валюту ";


    public String launch(String text) {

        String[] args = text.split(SPLIT_SEPARATOR);
        StringBuilder result = new StringBuilder();
        try {
            CommandLine commandLine = ParserInput.parseCommand(args); // получать в Bot и передать в валидатор
            String alg = commandLine.getOptionValue(ALGORITHM_COMMAND);
            Algorithm algorithm = ParserInput.parseAlgorithm(alg);
            GraphFormation graphFormation = new GraphFormation();
            Plot plt = graphFormation.getPlot();

            int currenciesIndex = parseCurrenciesIndex(args);
            String[] currencyArray = CurrencyFile.parseCurrency(args[currenciesIndex]);

            RateService rateService = new RateService();

            for (String currency : currencyArray) {
                CurrencyFile currencyString = CurrencyFile.parseCurrencyType(currency);
                if (currency == null) {
                    return ERROR_CURRENCY_MESSAGE + currencyString;
                }
                if (commandLine.hasOption(DATE_COMMAND)) {
                    String date = commandLine.getOptionValue(DATE_COMMAND);
                    result.append(currency + LINE_BREAK + rateService.calculateRate(currencyString, LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")), algorithm));
                } else if (commandLine.hasOption(PERIOD_COMMAND)) {
                    String periodStr = commandLine.getOptionValue(PERIOD_COMMAND);
                    Period period = parseCalculateType(periodStr);
                    if (commandLine.getOptionValue(OUTPUT_COMMAND).equalsIgnoreCase(OUTPUT_COMMAND_LIST)) {
                        result.append(currency + LINE_BREAK + rateService.calculateRate(currencyString, period, algorithm) + LINE_BREAK);
                    } else if (commandLine.getOptionValue(OUTPUT_COMMAND).equalsIgnoreCase(OUTPUT_COMMAND_GRAPH)) {
                        String colorCurrency = graphFormation.graphColor(currencyString);
                        graphFormation.getGraphLine(plt, colorCurrency, currencyString, period, algorithm);
                    }
                }
            }
            if (commandLine.getOptionValue(OUTPUT_COMMAND).equalsIgnoreCase(OUTPUT_COMMAND_GRAPH)) {
                getPhoto(plt);
            }
        } catch (Exception exception) {
            return exception.getMessage();

        }
        return result.toString();
    }

    private void getPhoto(Plot plt) throws PythonExecutionException, IOException {
        plt.legend();
        plt.savefig(PHOTO_PATH).dpi(PHOTO_SIZE);
        plt.executeSilently();
    }
}
