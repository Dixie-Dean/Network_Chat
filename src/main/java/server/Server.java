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


                System.out.println("Connection established - " + clientHandler);
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
}