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
    private static Thread clientThread;
    private String username;
    private Connection connection;

    private Client() {
        setUsername();
        try {
            connection = new Connection(this, new Socket(readHost(), readPort()));
            while (!clientThread.isInterrupted()) {
                String message = SCANNER.nextLine();

                switch (message) {
                    case EXIT -> {
                        disconnection(connection);
                        connection.disconnect();
                    }
                    case SETTINGS -> displaySettings();
                    default -> connection.sendMessage(username + ": " + message);
                }

            }
        } catch (IOException exception) {
            exceptionOccurred(connection, exception);
        }
    }

    public static void main(String[] args) {
        clientThread = new Thread(Client::new);
        clientThread.start();
    }

    private void setUsername() {
        System.out.print("Enter your username: ");
        username = SCANNER.nextLine();
        System.out.println("Username confirmed!");
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
}
