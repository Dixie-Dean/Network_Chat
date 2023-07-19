package server;

import settings.SettingsConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Server extends SettingsConfigurator {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final LinkedList<ClientHandler> CLIENT_HANDLERS = new LinkedList<>();

    public Server() {
        configureSettings(SCANNER);
        try (ServerSocket serverSocket = new ServerSocket(readPort())) {
            System.out.println("Server is running...");

            while (!serverSocket.isClosed()) {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                CLIENT_HANDLERS.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException exception) {
            System.out.println("Server IOException: " + exception.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    public static List<ClientHandler> getClientHandlersList() {
        return CLIENT_HANDLERS;
    }

//    @Override
//    public void messageReceived(String message) {
//        if (message != null) {
//            distribute(message);
//        }
//    }
//
//    @Override
//    public void exceptionOccurred(Connection connection, Exception exception) {
//        System.out.println("Connection exception: " + connection + " | " + exception.getMessage());
//    }
//
//    @Override
//    public void connectionEstablished(Connection connection) {
//        connections.add(connection);
//        System.out.println("Client connected: " + connection + " | Clients on server: " + connections.size());
//        distribute("Clients on server: " + connections.size());
//    }
//
//    @Override
//    public void disconnection(Connection connection) {
//        connections.remove(connection);
//        System.out.println("Client disconnected: " + connection + " | Clients on server: " + connections.size());
//        distribute("Clients on server: " + connections.size());
//    }
//
//    private void distribute(String message) {
//        for (Connection connection : connections) {
//            connection.sendMessage(message);
//        }
//    }
}