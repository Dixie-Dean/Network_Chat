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
        try (ServerSocket serverSocket = new ServerSocket(8088)) {
            while (true) {
                new Connection(this, serverSocket.accept());
            }
        } catch (IOException exception) {
            System.out.println("ServerSocket exception: " + exception.getMessage());
        }
    }

    @Override
    public void messageReceived(Connection connection, String message) {
        distribute(message);
    }

    @Override
    public void exceptionOccurred(Connection connection, Exception exception) {
        System.out.println("Connection exception: " + connection + " | " + exception.getMessage());
    }

    @Override
    public void connectionEstablished(Connection connection) {
        connections.add(connection);
        distribute(connection + " entered!");
    }

    @Override
    public void disconnection(Connection connection) {
        connections.remove(connection);
        distribute(connection + " left!");
    }

    private void distribute(String message) {
        for (Connection connection : connections) {
            connection.sendMessage(message);
        }
    }
}