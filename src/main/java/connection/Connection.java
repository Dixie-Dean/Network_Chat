package connection;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ConnectionObserver observer;
    private Thread thread;

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

    public synchronized void sendMessage(String message) {
        try {
            out.write(message + "\r\n");
            out.flush();
        } catch (IOException exception) {
            observer.exceptionOccurred(this, exception);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        observer.disconnection(this);
        try {
            socket.close();
        } catch (IOException exception) {
            observer.exceptionOccurred(this, exception);
        }
    }

    @Override
    public String toString() {
        return socket.getLocalAddress() + " | " + socket.getPort();
    }
}
