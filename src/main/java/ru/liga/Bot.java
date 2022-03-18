package ru.liga;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Log4j
public class Bot extends TelegramLongPollingCommandBot {
    private static final String USERNAME = "GetExchangeRateForecast";
    private static String TOKEN;


    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        User user = msg.getFrom();
        String text = msg.getText();

        String[] args = text.split(" ");
        String reply;
        InWorkToScatter inWorkToScatter = new InWorkToScatter();
        try {
            if (text.contains("graph")) {
                reply = inWorkToScatter.launch(args);
                SendPhoto sendPhoto = new SendPhoto();
                File file = new File(InWorkToScatter.PHOTO_PATH);
                InputFile inputFile = new InputFile(file);
                sendPhoto.setPhoto(inputFile);
                sendPhoto.setChatId(chatId.toString());
                String userName = (user.getUserName() != null) ? user.getUserName() :
                        String.format("%s %s", user.getLastName(), user.getFirstName());
                sendMessageToChat(chatId, userName, file);

            } else {
                reply = inWorkToScatter.launch(args);
            }
            String userName = (user.getUserName() != null) ? user.getUserName() :
                    String.format("%s %s", user.getLastName(), user.getFirstName());
            sendMessageToChat(chatId, userName, reply);
        } catch (Exception e) {
            log.fatal(e);
        }
    }

    private void sendMessageToChat(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
                    userName));
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
            log.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
                    userName));
            e.printStackTrace();

        }
    }
    public static void getTokenConfig() {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
            TOKEN = properties.getProperty("token");
        } catch (IOException e) {
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
