package ru.liga.service;

import org.apache.commons.cli.*;
import ru.liga.algorithm.ActualAlgorithm;
import ru.liga.algorithm.Algorithm;
import ru.liga.algorithm.LinearRegression;
import ru.liga.algorithm.MysticAlgorithm;

public class ParserInput {
    public static CommandLine parseCommand(String[] args) {
        Options options = new Options();
        Option option1 = Option.builder("date")
                .hasArg()
                .argName("дата в формате DD.mm.yyyy")
                .desc("Прогнозируемая дата")
                .build();
        Option option2 = Option.builder("period")
                .hasArg()
                .argName("запрашиваемый период")
                .desc("week, month")
                .build();
        Option option3 = Option.builder("alg")
                .hasArg()
                .argName("алгоритм")
                .desc("actual, mystic, line_regression ")
                .required()
                .build();
        Option option4 = Option.builder("output")
                .hasArg()
                .argName("output_type")
                .desc("способ вывода данных: graph, list")
                .build();
        options.addOption(option1)
                .addOption(option2)
                .addOption(option3)
                .addOption(option4);

        DefaultParser defaultParser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = defaultParser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("ExchangeRateForecast", options);
        }
        return commandLine;
    }

    public Algorithm parseAlgorithm(String alg) {
        Algorithm algorithm;
        switch (alg) {
            case "actual": algorithm =new ActualAlgorithm();
            break;
            case "mystic": algorithm = new MysticAlgorithm();
            break;
            case "linear_regression": algorithm = new LinearRegression();
            break;
            default: throw new IllegalArgumentException("No such algorithm: [%s]" + alg);
        };
        return algorithm;
    }

    public String[] parseCurrency(String currency) {
        String[] currencyArray = currency.split (",");
        return currencyArray;
    }
}
