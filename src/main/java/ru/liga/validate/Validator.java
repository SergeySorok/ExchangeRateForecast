package ru.liga.validate;

import org.apache.commons.cli.CommandLine;

public class Validator {
    private Validator() {
    }

    public static void generalValidateCommand(CommandLine commandLine) {
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
