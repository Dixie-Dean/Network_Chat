package server;

import connection.Connection;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) {
        new Server();
    }

    private Server() {
        try (ServerSocket serverSocket = new ServerSocket(8088)) {
            while (true) {
                new Connection(serverSocket.accept());
            }
        } catch (IOException exception) {
            System.out.println("ServerSocket exception: " + exception.getMessage());
        }
    }
}