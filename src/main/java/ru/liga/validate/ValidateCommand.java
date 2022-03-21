package ru.liga.validate;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import ru.liga.exception.CommandLineException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidateCommand {

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
        if (count != 2) {
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
                if(option.getValue().equalsIgnoreCase("tomorrow")) {
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
}
