package connection;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final ConnectionObserver observer;

    public Connection(ConnectionObserver observer, Socket socket) throws IOException {
        this.observer = observer;
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        observer.connectionEstablished(this);
    }

    public synchronized void sendMessage(String message) {
        try {
            writer.write(message + "\r\n");
            writer.flush();
        } catch (IOException exception) {
            observer.exceptionOccurred(this, exception);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        observer.disconnection(this);
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException exception) {
            observer.exceptionOccurred(this, exception);
        }
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                observer.messageReceived(reader.readLine());
            }
        } catch (IOException e) {
            disconnect();
        }
    }

    @Override
    public String toString() {
        return "" + socket.getPort();
    }
}
