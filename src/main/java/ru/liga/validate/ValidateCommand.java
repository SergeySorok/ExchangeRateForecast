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

public class ValidateCommand {
    private static final int MAX_CURRENCIES_SIZE = 5;
    private static final int ARRAY_ELEMENT_WITH_RATE = 0;
    private static final String RATE_COMMAND = "rate";

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
            if (option.getOpt().equalsIgnoreCase("date")
                    || option.getOpt().equalsIgnoreCase("output"))
                count++;
        }
        if (count > 1) {
            throw new CommandLineException("Невозможно использовать команду output совместно с командой date. Пожалуйста, введите команду корректно");

        }
    }

    public void incompatibleCommandsDateAndPeriod(CommandLine commandLine) throws CommandLineException {
        int count = 0;
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("date")
                    || option.getOpt().equalsIgnoreCase("period"))
                count++;
        }
        if (count != 1) {
            throw new CommandLineException("Невозможно использовать команду date совместно с командой period. Пожалуйста, введите команду корректно");

        }
    }

    public void emptyGroupCommands(CommandLine commandLine) throws CommandLineException {
        int count = 0;
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("period")
                    || option.getOpt().equalsIgnoreCase("output")) {
                count++;
            }
        }
        if (count == 1) {
            throw new CommandLineException("Невозможно использовать команду period без выбора команды output (list/graph). Пожалуйста, введите команду корректно");

        }
    }

    public void emptyAlgCommands(CommandLine commandLine) throws CommandLineException {
        int count = 0;
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("alg")) {
                count++;
            }
        }
        if (count != 1) {
            throw new CommandLineException("Вы не ввели команду alg. Пожалуйста, введите команду корректно");
        }
    }

    public void invalidDate(CommandLine commandLine) throws CommandLineException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("date")) {
                if (option.getValue().equalsIgnoreCase("tomorrow")) {
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
            if (option.getOpt().equalsIgnoreCase("period")) {
                if (option.getValue() == null) {
                    throw new CommandLineException("Не указано значение period. Пожалуйста, введите команду корректно");
                }
                if (option.getValue().equalsIgnoreCase("week") || option.getValue().equalsIgnoreCase("month")) {
                    return;
                } else {
                    throw new CommandLineException("Указанное значение команды period некорректно. Вы можете выбрать week или month. Пожалуйста, введите команду корректно");
                }
            }
        }
    }

    public void invalidAlgorithm(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("alg")) {
                if (option.getValue() == null) {
                    throw new CommandLineException("Значение команды alg неуказанно. Вы можете выбрать actual, mystic или linear_regression. Пожалуйста, введите команду корректно");
                }
                if (option.getValue().equalsIgnoreCase("actual") || option.getValue().equalsIgnoreCase("mystic")
                        || option.getValue().equalsIgnoreCase("linear_regression")) {
                    return;
                } else {
                    throw new CommandLineException("Указанное значение команды alg некорректно. Вы можете выбрать week или month. Пожалуйста, введите команду корректно");
                }
            }
        }
    }

    public void invalidOutput(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("output")) {
                if (option.getValue() == null) {
                    throw new CommandLineException("Значение команды output неуказанно. Вы можете выбрать list или output. Пожалуйста, введите команду корректно");
                }
                if (option.getValue().equalsIgnoreCase("list") || option.getValue().equalsIgnoreCase("graph")) {
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
