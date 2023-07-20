package server;

public interface ClientHandlerObserver {
    void onMsgReceived(ClientHandler clientHandler, String msg);

    void onConnection(ClientHandler clientHandler);

    void onDisconnection(ClientHandler clientHandler);
}
