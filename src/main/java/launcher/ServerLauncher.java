package launcher;

import server.Server;

import java.util.Scanner;

public class ServerLauncher {
    public static void main(String[] args) {
        Server server = new Server();
        server.configureSettings(new Scanner(System.in));
        server.launch();
    }
}
