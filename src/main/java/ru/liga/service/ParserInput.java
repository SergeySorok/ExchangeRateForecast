package ru.liga.service;

import org.apache.commons.cli.*;
import ru.liga.algorithm.ActualAlgorithm;
import ru.liga.algorithm.Algorithm;
import ru.liga.algorithm.LinearRegression;
import ru.liga.algorithm.MysticAlgorithm;
import ru.liga.currency.CurrencyFile;

import java.util.StringJoiner;

public class ParserInput {
    public static CommandLine parseCommand(String[] args) throws ParseException {
        boolean hasError = false;
        Options options = new Options();
        Option dateOption = Option.builder("date")
                .hasArg()
                .argName("дата в формате dd.MM.yyyy")
                .desc("Прогнозируемая дата")
                .build();
        Option periodOption = Option.builder("period")
                .hasArg()
                .argName("запрашиваемый период")
                .desc("week, month")
                .build();
        Option algorithmOption = Option.builder("alg")
                .hasArg()
                .argName("алгоритм")
                .desc("actual, mystic, line_regression ")
                .required()
                .build();
        Option outputOption = Option.builder("output")
                .hasArg()
                .argName("output_type")
                .desc("способ вывода данных: graph, list")
                .build();
        options.addOption(dateOption)
                .addOption(periodOption)
                .addOption(algorithmOption)
                .addOption(outputOption);

        DefaultParser defaultParser = new DefaultParser();

        StringJoiner errorMessage = new StringJoiner("\n");

        CommandLine commandLine = null;
        try {
            commandLine = defaultParser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            hasError = true;
            errorMessage.add(e.getMessage());
        }

        for (Option o : options.getOptions()) {
            if (commandLine.getOptionProperties(o).size() > 1) {
                hasError = true;
                errorMessage.add(String.format("Введена некорректно опция [%s]", o.getOpt()));
            }
        }

        if (hasError) {
            throw new ParseException(errorMessage.toString());
        }

        return commandLine;
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
                if (args[i].replace(' ', '0').indexOf(currency.name()) != -1) {
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
