package connection;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private Thread thread = null;
    private final ConnectionObserver observer;

    public Connection(ConnectionObserver observer, Socket socket) throws IOException {
        this.observer = observer;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        thread = new Thread(() -> {
            try {
                observer.connectionEstablished(this);
                while (!thread.isInterrupted()) {
                    observer.messageReceived(this, in.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    public Connection(ConnectionObserver observer, String ip, int port) throws IOException {
        this(observer, new Socket(ip, port));
    }

    public synchronized void sendMessage(String message) {
        try {
            out.write(message  + "\r\n");
            out.flush();
        } catch (IOException exception) {
            observer.exceptionOccurred(this, exception);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException exception) {
            observer.exceptionOccurred(this, exception);
        }
    }

    @Override
    public String toString() {
        return "Connection: " + socket.getInetAddress() + " : " + socket.getPort();
    }
}
