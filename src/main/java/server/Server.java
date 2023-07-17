package server;

import connection.Connection;
import connection.ConnectionObserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Server implements ConnectionObserver {
    public static void main(String[] args) {
        new Server();
    }

    private final LinkedList<Connection> connections = new LinkedList<>();

    private Server() {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(8088)) {
            while (true) {
                new Connection(this, serverSocket.accept());
            }
        } catch (IOException exception) {
            System.out.println("Server IOException: " + exception.getMessage());
        }
    }

    @Override
    public void messageReceived(Connection connection, String message) {
        distribute(message);
    }

    @Override
    public void exceptionOccurred(Connection connection, Exception exception) {
        System.out.println("[S]Connection exception: " + connection + " | " + exception.getMessage());
    }

    @Override
    public void connectionEstablished(Connection connection) {
        connections.add(connection);
        System.out.println("Client connected: " + connection + " | Clients on server: " + connections.size());
        distribute("Clients on server: " + connections.size());
    }

    @Override
    public void disconnection(Connection connection) {
        connections.remove(connection);
        System.out.println("Client disconnected: " + connection + " | Clients on server: " + connections.size());
        distribute("Clients on server: " + connections.size());
    }

    private void distribute(String message) {
        for (Connection connection : connections) {
            connection.sendMessage(message);
        }
    }
}