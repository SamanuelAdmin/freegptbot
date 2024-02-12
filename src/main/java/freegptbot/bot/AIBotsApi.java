package freegptbot.bot;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class AIBotsApi {
    public static String makeRequest(String request, String ip, int port) {
        try (
                Socket mainSocket = new Socket(ip, port);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                mainSocket.getInputStream()
                        )
                );

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                                mainSocket.getOutputStream()
                        )
                );
        ) {
            writer.write(request);
            writer.newLine();
            writer.flush();

            String response = "";

            while (true) {
                String buffer = reader.readLine();
                if (buffer == null) break;

                response += buffer;
            }

            return response;
        } catch (IOException e) {
            return "Error with response: " + e.getMessage();
        }
    }
}
