package client;

import connection.Connection;
import connection.ConnectionObserver;
import settings.SettingsHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends SettingsHandler implements ConnectionObserver {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Thread thread;
    private static String username;
    private Connection connection;

    private Client() {
        try {
            connection = new Connection(this, new Socket(readHost(), readPort()));
            Thread.sleep(10);
            while (!thread.isInterrupted()) {

                if (username == null) {
                    System.out.print("Enter your username: ");
                    username = SCANNER.nextLine();
                    System.out.println("Username confirmed!");
                }

                String message = SCANNER.nextLine();
                if (message.equals("/exit")) {
                    disconnection(connection);
                    connection.disconnect();
                } else {
                    connection.sendMessage(username + ": " + message);
                }
            }
        } catch (IOException exception) {
            exceptionOccurred(connection, exception);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        thread = new Thread(Client::new);
        thread.start();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void messageReceived(Connection connection, String message) {
        System.out.println(message);
    }

    @Override
    public void exceptionOccurred(Connection connection, Exception exception) {
        System.out.println("[C]Connection exception: " + connection + " | " + exception.getMessage());
    }

    @Override
    public void connectionEstablished(Connection connection) {
        System.out.println("Welcome to chat!");
    }

    @Override
    public void disconnection(Connection connection) {
        System.out.println("Goodbye!");
    }
}
