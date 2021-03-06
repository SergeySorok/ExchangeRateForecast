package ru.liga.validate;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import ru.liga.currency.CurrencyFile;
import ru.liga.exception.CommandLineException;
import ru.liga.service.ParserInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.liga.CommandProcessingLogic.*;

public class ValidateCommand {
    private static final int MAX_CURRENCIES_SIZE = 5;
    private static final int ARRAY_ELEMENT_WITH_RATE = 0;
    private static final String RATE_COMMAND = "rate";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DATE_COMMAND_VALUE_TOMORROW = "tomorrow";
    private static final String PERIOD_COMMAND_VALUE_WEEK = "week";
    private static final String PERIOD_COMMAND_VALUE_MONTH = "month";
    private static final String ALGORITHM_COMMAND_VALUE_ACTUAL = "actual";
    private static final String ALGORITHM_COMMAND_VALUE_MYSTIC = "mystic";
    private static final String ALGORITHM_COMMAND_VALUE_LINEAR_REGRESSION = "linear_regression";


    public void commandDuplicate(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (commandLine.getOptionProperties(option).size() > 1) {
                throw new CommandLineException("Дублирование команд. Пожалуйста, введите команду корректно");
            }
        }
    }

    public void incompatibleCommandsDateAndOutput(CommandLine commandLine) throws CommandLineException {
        int count = 0;
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(DATE_COMMAND)
                    || option.getOpt().equalsIgnoreCase(OUTPUT_COMMAND))
                count++;
        }
        if (count > 1) {
            throw new CommandLineException("Невозможно использовать команду " + OUTPUT_COMMAND + " совместно с командой " + DATE_COMMAND + ". Пожалуйста, введите команду корректно");

        }
    }

    public void incompatibleCommandsDateAndPeriod(CommandLine commandLine) throws CommandLineException {
        int count = 0;
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(DATE_COMMAND)
                    || option.getOpt().equalsIgnoreCase(PERIOD_COMMAND))
                count++;
        }
        if (count != 1) {
            throw new CommandLineException("Невозможно использовать команду " + DATE_COMMAND + " совместно с командой " + PERIOD_COMMAND + ". Пожалуйста, введите команду корректно");

        }
    }

    public void emptyGroupCommands(CommandLine commandLine) throws CommandLineException {
        int count = 0;
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(PERIOD_COMMAND)
                    || option.getOpt().equalsIgnoreCase(OUTPUT_COMMAND)) {
                count++;
            }
        }
        if (count == 1) {
            throw new CommandLineException("Невозможно использовать команду " + PERIOD_COMMAND + " без выбора команды output ( " + OUTPUT_COMMAND_LIST + "/" + OUTPUT_COMMAND_GRAPH + "). Пожалуйста, введите команду корректно");

        }
    }

    public void emptyAlgCommands(CommandLine commandLine) throws CommandLineException {
        int count = 0;
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(ALGORITHM_COMMAND)) {
                count++;
            }
        }
        if (count != 1) {
            throw new CommandLineException("Вы не ввели команду alg. Пожалуйста, введите команду корректно");
        }
    }

    public void invalidDate(CommandLine commandLine) throws CommandLineException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(DATE_COMMAND)) {
                if (option.getValue().equalsIgnoreCase(DATE_COMMAND_VALUE_TOMORROW)) {
                    return;
                }
                if (option.getValue() == null) {
                    throw new CommandLineException("Значение команды date некорректно. Пожалуйста, введите команду корректно");
                }
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(option.getValue(), dateFormat);
                } catch (CommandLineException e) {
                    throw new CommandLineException("Некорректный формат введенной даты. Необходимый формат dd.MM.yyyy. Пожалуйста, введите команду корректно");
                }
                if (localDate.isBefore(LocalDate.now().plusDays(1))) {
                    throw new CommandLineException("Введенная дата некорретна. Запрашиваемая дата должна быть от завтрашнего дня и позднее. Пожалуйста, введите команду корректно");

                }
            }
        }
    }

    public void invalidPeriod(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(PERIOD_COMMAND)) {
                if (option.getValue() == null) {
                    throw new CommandLineException("Не указано значение period. Пожалуйста, введите команду корректно");
                }
                if (option.getValue().equalsIgnoreCase(PERIOD_COMMAND_VALUE_WEEK) || option.getValue().equalsIgnoreCase(PERIOD_COMMAND_VALUE_MONTH)) {
                    return;
                } else {
                    throw new CommandLineException("Указанное значение команды period некорректно. Вы можете выбрать week или month. Пожалуйста, введите команду корректно");
                }
            }
        }
    }

    public void invalidAlgorithm(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(ALGORITHM_COMMAND)) {
                if (option.getValue() == null) {
                    throw new CommandLineException("Значение команды alg неуказанно. Вы можете выбрать actual, mystic или linear_regression. Пожалуйста, введите команду корректно");
                }
                if (option.getValue().equalsIgnoreCase(ALGORITHM_COMMAND_VALUE_ACTUAL) || option.getValue().equalsIgnoreCase(ALGORITHM_COMMAND_VALUE_MYSTIC)
                        || option.getValue().equalsIgnoreCase(ALGORITHM_COMMAND_VALUE_LINEAR_REGRESSION)) {
                    return;
                } else {
                    throw new CommandLineException("Указанное значение команды alg некорректно. Вы можете выбрать week или month. Пожалуйста, введите команду корректно");
                }
            }
        }
    }

    public void invalidOutput(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase(OUTPUT_COMMAND)) {
                if (option.getValue() == null) {
                    throw new CommandLineException("Значение команды output неуказанно. Вы можете выбрать list или output. Пожалуйста, введите команду корректно");
                }
                if (option.getValue().equalsIgnoreCase(OUTPUT_COMMAND_LIST) || option.getValue().equalsIgnoreCase(OUTPUT_COMMAND_GRAPH)) {
                    return;
                } else {
                    throw new CommandLineException("Указанное значение команды output некорректно. Вы можете выбрать week или month. Пожалуйста, введите команду корректно");
                }
            }
        }
    }


    public void currenciesValidate(CommandLine commandLine) throws CommandLineException {
        String[] args = Arrays.stream(commandLine.getArgs())
                .toArray(String[]::new);
        if (!args[ARRAY_ELEMENT_WITH_RATE].equalsIgnoreCase(RATE_COMMAND)) {
            throw new CommandLineException("Команда rate введена не корректно");
        }
        String currencies = args[ParserInput.parseCurrenciesIndex(args)];
        String[] currenciesArray = currencies.split(",+");
        List<String> currenciesString = Arrays.asList(currenciesArray);
        List<String> uniqueCurrencies = currenciesString.stream()
                .distinct()
                .collect(Collectors.toList());
        if (uniqueCurrencies.size() > MAX_CURRENCIES_SIZE || uniqueCurrencies.size() != currenciesString.size()) {
            throw new CommandLineException("Валюта введена некорректно");
        }
        for (String uniqueCurrency : uniqueCurrencies) {
            try {
                CurrencyFile.valueOf(uniqueCurrency);
            } catch (IllegalArgumentException e) {
                throw new CommandLineException("Валюта введена некорректно");
            }
        }
    }
}
