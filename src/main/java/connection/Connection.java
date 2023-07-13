package connection;

import java.io.*;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private Thread thread;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        thread = new Thread(() -> {
            try {
                in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    public synchronized void sendMessage(String message) {
        try {
            out.write(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}