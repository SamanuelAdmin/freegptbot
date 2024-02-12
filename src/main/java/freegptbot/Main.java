package freegptbot;


import freegptbot.bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.Socket;

public class Main {
    public static String baseMessage = "Hello! How can I assist you today?"; // you can write this whatever you want

    public static String USERNAME = ""; // bot`s username
    public static String TOKEN = ""; // bot`s token
    public static String chatGPTServerIP = "127.0.0.1"; // dont touch it!
    public static int chatGPTServerPORT = 63131; // i said no!!!


    public static void main(String[] args) {
        // waiting before ai api server will be started

        System.out.print("Waiting...");
        while (true) {
            try {
                new Socket(
                        chatGPTServerIP, chatGPTServerPORT
                );

                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                    System.out.print(".");
                } catch (Exception q) {}
            }
        }
        System.out.println();

        try {
            TelegramBotsApi telegramBotApi = new TelegramBotsApi(
                    DefaultBotSession.class
            );

            telegramBotApi.registerBot(new TelegramBot()); // adding bot
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}