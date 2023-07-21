package client;

import disconnection.Disconnection;
import settings.SettingsHandler;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends SettingsHandler implements Disconnection {
    private static final Scanner SCANNER = new Scanner(System.in);
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
            receiving();
            sending();
        } catch (IOException exception) {
            disconnect(socket, reader, writer);
        }
    }

    public static void main(String[] args) {
        Thread clientThread = new Thread(Client::new);
        clientThread.start();
    }

    private void setUsername() {
        System.out.print("Enter your username, please: ");
        this.username = SCANNER.nextLine();
        System.out.printf("Welcome to the chat, %s!\n", username);
        sendMessage(username);
    }

    private void sending() {
        socketLoop:
        while (socket.isConnected()) {
            String messageToSend = SCANNER.nextLine();
            switch (messageToSend) {
                case SETTINGS -> displaySettings();
                case EXIT -> {
                    exit(messageToSend);
                    break socketLoop;
                }
                default -> sendMessage(username + ": " + messageToSend);
            }
        }
    }

    private void receiving() {
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

    private void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException exception) {
            disconnect(socket, reader, writer);
        }
    }

    private void exit(String exitMessage) {
        sendMessage(exitMessage);
        disconnect(socket, reader, writer);
    }
}
