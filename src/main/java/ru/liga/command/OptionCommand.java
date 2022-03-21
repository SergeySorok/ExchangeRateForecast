package ru.liga.command;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionCommand {
    public static Options getOptions() {
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
        return options;
    }
}
