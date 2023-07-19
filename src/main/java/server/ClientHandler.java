package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientUsername = setClientName();
            distribute("SERVER: " + clientUsername + "has entered the chat!");
        } catch (IOException exception) {
            closeEverything(socket, reader, writer);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String messageFromClient = reader.readLine();
                distribute(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, reader, writer);
                break;
            }
        }
    }

    private String setClientName() {
        sendString("Enter your username, please: ");
        try {
            return reader.readLine();
        } catch (IOException exception) {
            closeEverything(socket, reader, writer);
        }
        return null;
    }

    private synchronized void sendString(String message) {
        try {
            writer.write(message + "\r\n");
            writer.flush();
        } catch (IOException exception) {
            closeEverything(socket, reader, writer);
        }
    }

    private void distribute(String message) {
        for (ClientHandler clientHandler : Server.getClientHandlersList()) {
            if (clientHandler.clientUsername.equals(this.clientUsername)) {
                clientHandler.sendString(message);
            }
        }
    }

    private void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (socket != null) {
                socket.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
