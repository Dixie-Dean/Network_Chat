package server;

import settings.SettingsConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Server extends SettingsConfigurator implements ClientHandlerObserver {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final LinkedList<ClientHandler> CLIENT_HANDLERS = new LinkedList<>();

    public Server() {
        configureSettings(SCANNER);
        try (ServerSocket serverSocket = new ServerSocket(readPort())) {
            System.out.println("Server is running...");

            while (!serverSocket.isClosed()) {
                ClientHandler clientHandler = new ClientHandler(this, serverSocket.accept());
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

    @Override
    public void onMsgReceived(ClientHandler clientHandler, String msg) {
        System.out.println("Message from " + clientHandler + ": " + msg);
    }

    @Override
    public void onConnection(ClientHandler clientHandler) {
        CLIENT_HANDLERS.add(clientHandler);
        System.out.println(clientHandler + " has entered the chat! Clients on server: " + CLIENT_HANDLERS.size());
    }

    @Override
    public void onDisconnection(ClientHandler clientHandler) {
        CLIENT_HANDLERS.remove(clientHandler);
        System.out.println(clientHandler + " has left the chat! Clients on server: " + CLIENT_HANDLERS.size());
    }
}