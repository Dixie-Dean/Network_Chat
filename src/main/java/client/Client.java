package client;

import connection.Connection;
import connection.ConnectionObserver;

import java.io.IOException;
import java.net.Socket;

public class Client implements ConnectionObserver {
    public static void main(String[] args) {
        new Thread(Client::new).start();
    }

    private static final String HOST = "192.168.1.64";
    private static final int PORT = 8088;
    private Connection connection;

    private Client() {
        try {
           connection = new Connection(this, new Socket(HOST, PORT));

        } catch (IOException exception) {
            exceptionOccurred(connection, exception);
        }
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
        System.out.println("Connection is ready...");
    }

    @Override
    public void disconnection(Connection connection) {
        System.out.println("Disconnected...");
    }
}
