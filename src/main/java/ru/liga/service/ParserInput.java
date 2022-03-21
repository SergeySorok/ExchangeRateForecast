package ru.liga.service;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ru.liga.algorithm.ActualAlgorithm;
import ru.liga.algorithm.Algorithm;
import ru.liga.algorithm.LinearRegression;
import ru.liga.algorithm.MysticAlgorithm;
import ru.liga.currency.CurrencyFile;

import java.util.Arrays;
import java.util.StringJoiner;

public class ParserInput {
    private static final String SPLIT_SEPARATOR = " ";

    public static CommandLine parseCommand(Options options, String text) throws ParseException {
        DefaultParser defaultParser = new DefaultParser();
        String[] args = text.split(SPLIT_SEPARATOR);
        String[] argsAfterFilter = Arrays.stream(args)
                .filter(x -> !x.equals(""))
                .filter(x -> !x.equals(","))
                .toArray(String[]::new);

        return defaultParser.parse(options, argsAfterFilter);
    }

    public static Algorithm parseAlgorithm(String alg) {
        switch (alg) {
            case "actual":
                return new ActualAlgorithm();
            case "mystic":
                return new MysticAlgorithm();
            case "linear_regression":
                return new LinearRegression();
            default:
                throw new IllegalArgumentException("No such algorithm: [%s]" + alg);
        }
    }

    public static int parseCurrenciesIndex(String[] args) {
        StringJoiner errorMessage = new StringJoiner("\n");
        int indexCurrencies = -1;

        for (int i = 0; i < args.length; i++) {
            for (CurrencyFile currency : CurrencyFile.values()) {
                if (args[i].replace(' ', '0').contains(currency.name())) {
                    indexCurrencies = i;
                    break;
                }
            }
        }
        if (indexCurrencies < 0) {
            errorMessage.add(String.format("Не указана валюта"));
            throw new RuntimeException();
        }
        return indexCurrencies;
    }
}
