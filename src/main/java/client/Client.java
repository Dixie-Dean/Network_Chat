package client;

import settings.SettingsHandler;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends SettingsHandler {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String EXIT = "/exit";
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
        } catch (IOException exception) {
            closeEverything(socket, reader, writer);
        }
    }

    private void setUsername() {
        System.out.print("Enter your username, please: ");
        this.username = SCANNER.nextLine();
        System.out.printf("Welcome to the chat, %s!", username);
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();
        } catch (IOException exception) {
            closeEverything(socket, reader, writer);
        }
    }

    private void sendMessage() {
        while (socket.isConnected()) {
            try {
                String messageToSend = SCANNER.nextLine();
                switch (messageToSend) {
                    case SETTINGS -> displaySettings();

                    case EXIT -> {
                        closeEverything(socket, reader, writer);
                    }

                    default -> {
                        writer.write(username + ": " + messageToSend);
                        writer.flush();
                    }
                }
            } catch (IOException exception) {
                closeEverything(socket, reader, writer);
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
                    closeEverything(socket, reader, writer);
                    break;
                }
            }
        }).start();
    }

    private void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.sendMessage();
        client.receiveMessage();
    }
}
