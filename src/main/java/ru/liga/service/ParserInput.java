package ru.liga.service;

import org.apache.commons.cli.*;
import ru.liga.algorithm.ActualAlgorithm;
import ru.liga.algorithm.Algorithm;
import ru.liga.algorithm.LinearRegression;
import ru.liga.algorithm.MysticAlgorithm;

public class ParserInput {
    public static CommandLine parseCommand(String[] args) {
        CommandLine commandLine = null;
        Options options = new Options();
        Option dateOption = Option.builder("date")
                .hasArg()
                .argName("дата в формате DD.mm.yyyy")
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

        try {
            commandLine = defaultParser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("ExchangeRateForecast", options);
        }
        return commandLine;
    }

    public static Algorithm parseAlgorithm(String alg) {
        Algorithm algorithm;
        switch (alg) {
            case "actual": algorithm = new ActualAlgorithm();
            break;
            case "mystic": algorithm = new MysticAlgorithm();
            break;
            case "linear_regression": algorithm = new LinearRegression();
            break;
            default: throw new IllegalArgumentException("No such algorithm: [%s]" + alg);
        };
        return algorithm;
    }

}
