package ru.liga;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.apache.commons.cli.CommandLine;
import ru.liga.algorithm.Algorithm;
import ru.liga.currency.CurrencyFile;
import ru.liga.service.GraphFormation;
import ru.liga.service.ParserInput;
import ru.liga.service.ResultPreparationService;

import java.io.IOException;
import java.util.Arrays;

import static ru.liga.service.ParserInput.parseCurrenciesIndex;


public class CommandProcessingLogic {
    public static final String DATE_COMMAND = "date";
    public static final String PERIOD_COMMAND = "period";
    private static final String OUTPUT_COMMAND = "output";
    private static final String OUTPUT_COMMAND_LIST = "list";
    private static final String OUTPUT_COMMAND_GRAPH = "graph";
    private static final String ALGORITHM_COMMAND = "alg";


    public String commandExecutor(CommandLine commandLine) {

        String[] args = Arrays.stream(commandLine.getArgs()).toArray(String[]::new);
        Algorithm algorithm = ParserInput.parseAlgorithm(commandLine.getOptionValue(ALGORITHM_COMMAND));
        String[] currencyArray = CurrencyFile.parseCurrency(args[parseCurrenciesIndex(args)]);
        StringBuilder result = new StringBuilder();
        GraphFormation graphFormation = new GraphFormation();
        Plot plt = graphFormation.getPlot();

        for (String currency : currencyArray) {
            CurrencyFile currencyString = CurrencyFile.parseCurrencyType(currency);
            if (commandLine.hasOption(DATE_COMMAND)) {
                result.append(ResultPreparationService.ResultPreparationDay(commandLine, currency, currencyString, algorithm));
            } else if (commandLine.hasOption(PERIOD_COMMAND)) {
                result.append(ResultPreparationService.ResultPreparationPeriod(commandLine, currency, currencyString, algorithm));
                if (commandLine.getOptionValue(OUTPUT_COMMAND).equalsIgnoreCase(OUTPUT_COMMAND_GRAPH)) {
                    graphFormation.getPhoto(plt);
                }
            }
        }
        return result.toString();
    }
}
