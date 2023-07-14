package client;

import connection.Connection;
import connection.ConnectionObserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements ConnectionObserver {
    private static Thread thread;

    public static void main(String[] args) {
        thread = new Thread(Client::new);
        thread.start();
    }

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String HOST = "192.168.1.64";
    private static final int PORT = 8088;
    private Connection connection;

    private Client() {
        try {
           connection = new Connection(this, new Socket(HOST, PORT));
           while (!thread.isInterrupted()) {
               String message = SCANNER.nextLine();
               if (message.equals("/exit")) {
                   disconnection(connection);
                   connection.disconnect();
               }
               connection.sendMessage(message);
           }
        } catch (IOException exception) {
            exceptionOccurred(connection, exception);
        }
    }

    @Override
    public void messageReceived(Connection connection, String message) {
        System.out.println("Message from " + connection + " | " + message);
    }

    @Override
    public void exceptionOccurred(Connection connection, Exception exception) {
        System.out.println("[C]Connection exception: " + connection + " | " + exception.getMessage());
    }

    @Override
    public void connectionEstablished(Connection connection) {
        System.out.println("Client connected: " + connection);
    }

    @Override
    public void disconnection(Connection connection) {
        System.out.println("Client disconnected: " + connection);
    }
}
