package server;

import settings.SettingsConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalTime;
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

    private String getTime() {
        return LocalTime.now().getHour() + ":" +  LocalTime.now().getMinute() + ":" +  LocalTime.now().getSecond();
    }

    @Override
    public void onMsgReceived(ClientHandler clientHandler, String msg) {
        System.out.println(getTime() + " | " + msg);
    }

    @Override
    public void onConnection(ClientHandler clientHandler) {
        CLIENT_HANDLERS.add(clientHandler);
        System.out.printf("%s | %s has entered the chat! Clients on server: %d\n",
                getTime(), clientHandler, CLIENT_HANDLERS.size());
    }

    @Override
    public void onDisconnection(ClientHandler clientHandler) {
        CLIENT_HANDLERS.remove(clientHandler);
        System.out.printf("%s | %s has left the chat! Clients on server: %d\n",
                getTime(), clientHandler, CLIENT_HANDLERS.size());
    }
}