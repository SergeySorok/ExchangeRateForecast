package ru.liga.service;

import com.github.sh0nk.matplotlib4j.Plot;
import org.apache.commons.cli.CommandLine;
import ru.liga.algorithm.Algorithm;
import ru.liga.calculate.Period;
import ru.liga.currency.CurrencyFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.liga.CommandProcessingLogic.DATE_COMMAND;
import static ru.liga.CommandProcessingLogic.PERIOD_COMMAND;
import static ru.liga.calculate.Period.parseCalculateType;

public class ResultPreparationService {
    private static final String DATE_COMMAND_TOMORROW = "tomorrow";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String LINE_BREAK = "\n";

    public static StringBuilder ResultPreparationDay(CommandLine commandLine, String currency, CurrencyFile currencyString, Algorithm algorithm) {
        RateService rateService = new RateService();
        StringBuilder result = new StringBuilder();
        String date;
        if (commandLine.getOptionValue(DATE_COMMAND).equalsIgnoreCase(DATE_COMMAND_TOMORROW)) {
            date = LocalDate.now().plusDays(Period.TOMORROW.getCalculationPeriod()).format(DATE_FORMAT);
        } else {
            date = commandLine.getOptionValue(DATE_COMMAND);
        }
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String calculateRate = rateService.calculateRate(currencyString, localDate, algorithm);
        result.append(currency + LINE_BREAK + calculateRate + LINE_BREAK + LINE_BREAK);
        return result;
    }

    public static StringBuilder resultPreparationPeriodList
            (CommandLine commandLine, String currency, CurrencyFile currencyString, Algorithm algorithm) {

        RateService rateService = new RateService();
        Period period = parseCalculateType(commandLine.getOptionValue(PERIOD_COMMAND));
        StringBuilder result = new StringBuilder();
        String calculateRate = rateService.calculateRate(currencyString, period, algorithm);
        result.append(currency + LINE_BREAK + calculateRate + LINE_BREAK);
        return result;
    }

  /*  public static Plot resultPreparationPeriodGraph
            (CommandLine commandLine, String currency, CurrencyFile currencyString, Algorithm algorithm, Plot plt) {
        RateService rateService = new RateService();
        Period period = parseCalculateType(commandLine.getOptionValue(PERIOD_COMMAND));
        StringBuilder result = new StringBuilder();
        String calculateRate = rateService.calculateRate(currencyString, period, algorithm);
        result.append(currency + LINE_BREAK + calculateRate + LINE_BREAK);
        return result;
    } */
}
