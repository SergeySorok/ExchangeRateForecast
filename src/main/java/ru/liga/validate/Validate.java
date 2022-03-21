package ru.liga.validate;

import org.apache.commons.cli.CommandLine;
import ru.liga.exception.CommandLineException;

public class Validate {
    public void generalValidateCommand(CommandLine commandLine) throws CommandLineException {
        ValidateCommand validateCommand = new ValidateCommand();
        validateCommand.commandDuplicate(commandLine);
        validateCommand.emptyGroupCommands(commandLine);
        validateCommand.emptyAlgCommands(commandLine);
        validateCommand.incompatibleCommandsDateAndOutput(commandLine);
        validateCommand.incompatibleCommandsDateAndPeriod(commandLine);
        validateCommand.invalidAlgorithm(commandLine);
        validateCommand.invalidDate(commandLine);
        validateCommand.invalidOutput(commandLine);
        validateCommand.invalidPeriod(commandLine);
        validateCommand.currenciesValidate(commandLine);
    }
}
