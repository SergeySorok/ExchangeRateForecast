package ru.liga.validate;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import ru.liga.exception.CommandLineException;
import ru.liga.service.ParserInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ValidateCommand {
    private static final int MAX_CURRENCIES_SIZE = 5;
    private static final int ARRAY_ELEMENT_WITH_RATE = 0;
    private static final String RATE_COMMAND = "rate";

    public void commandDuplicate(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (commandLine.getOptionProperties(option).size() > 1) {
                throw new CommandLineException();
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
            throw new CommandLineException();

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
            throw new CommandLineException();

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
            throw new CommandLineException();

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
            throw new CommandLineException();
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
                    throw new CommandLineException();
                }
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(option.getValue(), dateFormat);
                } catch (CommandLineException e) {
                    throw new CommandLineException();
                }
                if (localDate.isBefore(LocalDate.now().plusDays(1))) {
                    throw new CommandLineException();

                }
            }
        }
    }

    public void invalidPeriod(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("period")) {
                if (option.getValue() == null) {
                    throw new CommandLineException();
                }
                if (option.getValue().equalsIgnoreCase("week") || option.getValue().equalsIgnoreCase("month")) {
                    return;
                } else {
                    throw new CommandLineException();
                }
            }
        }
    }

    public void invalidAlgorithm(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("alg")) {
                if (option.getValue() == null) {
                    throw new CommandLineException();
                }
                if (option.getValue().equalsIgnoreCase("actual") || option.getValue().equalsIgnoreCase("mystic")
                        || option.getValue().equalsIgnoreCase("linear_regression")) {
                    return;
                } else {
                    throw new CommandLineException();
                }
            }
        }
    }

    public void invalidOutput(CommandLine commandLine) throws CommandLineException {
        for (Option option : commandLine.getOptions()) {
            if (option.getOpt().equalsIgnoreCase("output")) {
                if (option.getValue() == null) {
                    throw new CommandLineException();
                }
                if (option.getValue().equalsIgnoreCase("list") || option.getValue().equalsIgnoreCase("graph")) {
                    return;
                } else {
                    throw new CommandLineException();
                }
            }
        }
    }


    public void currenciesValidate(CommandLine commandLine) throws CommandLineException {
        String[] args = Arrays.stream(commandLine.getArgs())
                .toArray(String[]::new);
        if (!args[ARRAY_ELEMENT_WITH_RATE].equalsIgnoreCase(RATE_COMMAND)) {
            throw new CommandLineException();
        }
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
}
