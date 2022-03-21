package ru.liga.bot;

import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.CommandLine;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.CommandProcessingLogic;
import ru.liga.Main;
import ru.liga.command.OptionCommand;
import ru.liga.exception.CommandLineException;
import ru.liga.service.GraphFormation;
import ru.liga.service.ParserInput;
import ru.liga.validate.Validator;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Log4j
public class Bot extends TelegramLongPollingCommandBot {
    private static final String OUTPUT_GRAPH_COMMAND = "graph";
    private String USERNAME;
    private String TOKEN;

    public Bot() {
        register(new StartCommand("start", "Старт"));

        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
            TOKEN = properties.getProperty("token");
            USERNAME = properties.getProperty("botName");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        User user = msg.getFrom();
        String text = msg.getText();
        String userName = getUserName(user);
        try {
            CommandLine commandLine = ParserInput.parseCommand(OptionCommand.getOptions(), text);
            Validator.generalValidateCommand(commandLine);
            CommandProcessingLogic commandProcessingLogic = new CommandProcessingLogic();
            String reply = commandProcessingLogic.commandExecutor(commandLine);
            if (text.contains(OUTPUT_GRAPH_COMMAND)) {
                File file = new File(GraphFormation.PHOTO_PATH);
                sendMessageToChat(chatId, userName, file);
            }
            sendMessageToChat(chatId, userName, reply);
        } catch (CommandLineException e) {
            sendMessageToChat(msg.getChatId(), msg.getFrom().getUserName(), "Вы ввели некорректную команду ");
        }
    }

    public static String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }



    public void sendMessageToChat(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToChat(Long chatId, String userName, File photo) {
        SendPhoto answer = new SendPhoto();
        answer.setChatId(chatId.toString());
        answer.setPhoto(new InputFile(photo));
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

}
