package client;

import disconnection.Disconnection;
import settings.SettingsHandler;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends SettingsHandler implements Disconnection {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String SETTINGS = "/settings";
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;
    private String username;


    private Client() {
        try {
            this.socket = new Socket(readHost(), readPort());
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            setUsername();
            receiveMessage();
            sendMessage();
        } catch (IOException exception) {
            disconnect(socket, reader, writer);
        }
    }

    private void setUsername() {
        System.out.print("Enter your username, please: ");
        this.username = SCANNER.nextLine();
        System.out.printf("Welcome to the chat, %s!\n", username);
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();
        } catch (IOException exception) {
            disconnect(socket, reader, writer);
        }
    }

    private void sendMessage() {
        while (socket.isConnected()) {
            try {
                String messageToSend = SCANNER.nextLine();
                switch (messageToSend) {
                    case SETTINGS -> displaySettings();

                    case EXIT -> {
                        notifyOfExit();
                        disconnect(socket, reader, writer);
                    }

                    default -> {
                        writer.write(username + ": " + messageToSend);
                        writer.newLine();
                        writer.flush();
                    }
                }
            } catch (IOException exception) {
                disconnect(socket, reader, writer);
            }
        }
    }

    private void receiveMessage() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String receivedMessage = reader.readLine();
                    System.out.println(receivedMessage);
                } catch (IOException e) {
                    disconnect(socket, reader, writer);
                    break;
                }
            }
        }).start();
    }

    private void notifyOfExit() {
        try {
            writer.write("/exit");
            writer.newLine();
            writer.flush();
        } catch (IOException exception) {
            disconnect(socket, reader, writer);
        }
    }

    public static void main(String[] args) {
        Thread clientThread = new Thread(Client::new);
        clientThread.start();
    }
}
