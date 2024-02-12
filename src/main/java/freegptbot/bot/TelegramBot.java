package freegptbot.bot;

import com.google.j2objc.annotations.ObjectiveCName;
import freegptbot.Main;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class TelegramBot extends TelegramLongPollingBot {
    public TelegramBot() {
        System.out.println(String.format("Bot %s has been started!", Main.USERNAME));
    }

    @Override
    public String getBotToken() {
        return Main.TOKEN;
    }

    @Override
    public String getBotUsername() {
        return Main.USERNAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // main code (checking for messages and send the response)

        TelegramBot current = this; // dublicate of class obj

        // every request will be processed in new thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String response;

                        SendMessage sendMessage = new SendMessage();
                        String request = update.getMessage().getText();

                        if (request.equals("/start")) { // if request is main (/start) command
                            response = Main.baseMessage;
                        } else {
                            response = AIBotsApi.makeRequest(request, Main.chatGPTServerIP, Main.chatGPTServerPORT);

                        }

                        sendMessage.setChatId(
                                update.getMessage().getChatId()
                        ); // send to current user
                        sendMessage.setText(response);
                        try {
                            current.execute(sendMessage); // sending message
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();


    }

}
