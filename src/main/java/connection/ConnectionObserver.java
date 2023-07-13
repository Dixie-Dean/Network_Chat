package connection;

public interface ConnectionObserver {
    void messageReceived(Connection connection, String message);
    void exceptionOccurred(Connection connection, Exception exception);
    void connectionEstablished(Connection connection);
    void disconnection(Connection connection);
}
