package connection;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ConnectionObserver observer;
    private Thread inputThread;

    public Connection(ConnectionObserver observer, Socket socket) throws IOException {
        this.observer = observer;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        inputThread = new Thread(() -> {
            try {
                observer.connectionEstablished(this);
                while (socket.isConnected()) {
                    observer.messageReceived(in.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        inputThread.start();
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
        observer.disconnection(this);
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException exception) {
            observer.exceptionOccurred(this, exception);
        }
    }

    @Override
    public String toString() {
        return "" + socket.getPort();
    }
}
