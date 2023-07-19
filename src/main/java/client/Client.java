package client;

import connection.Connection;
import connection.ConnectionObserver;
import settings.SettingsHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends SettingsHandler implements ConnectionObserver {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String EXIT = "/exit";
    private static final String SETTINGS = "/settings";
    private static Thread outputThread;
    private String username;
    private Connection connection;

    private Client() {
        setUsername();
        try {
            connection = new Connection(this, new Socket(readHost(), readPort()));
            while (!outputThread.isInterrupted()) {
                String messageToSend = SCANNER.nextLine();
                switch (messageToSend) {
                    case EXIT -> {
                        connection.disconnect();
                        outputThread.interrupt();
                    }
                    case SETTINGS -> displaySettings();
                    default -> connection.sendMessage(username + ": " + messageToSend);
                }

            }
        } catch (IOException exception) {
            exceptionOccurred(connection, exception);
        }
    }

    private void setUsername() {
        System.out.print("Enter your username: ");
        username = SCANNER.nextLine();
    }

    @Override
    public void messageReceived(String message) {
        System.out.println(message);
    }

    @Override
    public void exceptionOccurred(Connection connection, Exception exception) {
        System.out.println("Connection exception: " + connection + " | " + exception.getMessage());
    }

    @Override
    public void connectionEstablished(Connection connection) {
        System.out.printf("Welcome to chat, %s!\n", username);
    }

    @Override
    public void disconnection(Connection connection) {
        System.out.printf("Goodbye, %s!", username);
    }

    public static void main(String[] args) {
        outputThread = new Thread(Client::new);
        outputThread.start();
    }
}
