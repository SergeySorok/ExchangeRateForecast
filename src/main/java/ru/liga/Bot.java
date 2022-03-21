package ru.liga;

import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.command.OptionCommand;
import ru.liga.exception.CommandLineException;
import ru.liga.service.GraphFormation;
import ru.liga.service.ParserInput;
import ru.liga.validate.Validate;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Log4j
public class Bot extends TelegramLongPollingCommandBot {
    private static final String OUTPUT_GRAPH_COMMAND = "graph";
    private String USERNAME;
    private String TOKEN;

    public Bot() {
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
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        try {
            CommandLine commandLine = ParserInput.parseCommand(OptionCommand.getOptions(), text);
            Validate validate = new Validate();
            validate.generalValidateCommand(commandLine);
            CommandProcessingLogic commandProcessingLogic = new CommandProcessingLogic();
            String reply = commandProcessingLogic.commandImplementation(commandLine);
            if (text.contains(OUTPUT_GRAPH_COMMAND)) {
                SendPhoto sendPhoto = new SendPhoto();
                File file = new File(GraphFormation.PHOTO_PATH);
                InputFile inputFile = new InputFile(file);
                sendPhoto.setPhoto(inputFile);
                sendPhoto.setChatId(chatId.toString());
                sendMessageToChat(chatId, userName, file);
            }
            sendMessageToChat(chatId, userName, reply);
        } catch (CommandLineException | ParseException | IOException | PythonExecutionException | TelegramApiException e) {
            try {
                sendMessageToChat(msg.getChatId(), msg.getFrom().getUserName(), "Вы ввели некорректную команду ");
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageToChat(Long chatId, String userName, String text) throws TelegramApiException {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        execute(answer);
    }

    private void sendMessageToChat(Long chatId, String userName, File photo) throws TelegramApiException {
        SendPhoto answer = new SendPhoto();
        answer.setChatId(chatId.toString());
        answer.setPhoto(new InputFile(photo));
        execute(answer);
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
