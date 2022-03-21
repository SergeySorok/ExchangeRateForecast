package ru.liga.validate;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import ru.liga.exception.CommandLineException;
import ru.liga.service.ParserInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidateText {
    private static final int MAX_CURRENCIES_SIZE = 5;

    public void currenciesValidate(String text) throws CommandLineException {
        String[] args = text.split(" ");
        String currencies = args[ParserInput.parseCurrenciesIndex(args)];
        if (currencies.contains(",,")) {
            throw new CommandLineException();
        }
        String[] currenciesArray = currencies.split(",");
        if (currenciesArray.length > MAX_CURRENCIES_SIZE) {
            throw new CommandLineException();
        }
        int countDuplicate;
        for (String currency : currenciesArray) {
            countDuplicate = 0;
            for (int i = 0; i < currenciesArray.length; i++) {
                if (currency.equals(currenciesArray[i])) {
                    countDuplicate++;
                    if (countDuplicate > 1) {
                        throw new CommandLineException();
                    }
                }
            }
        }
    }

    public void rateValidate(String text) throws CommandLineException {
        String[] args = text.split(" ");
        if (args[0].equalsIgnoreCase("rate")) {
            throw new CommandLineException();
        }
    }
}
