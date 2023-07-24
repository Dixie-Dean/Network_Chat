package settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

class SettingsHandlerTest {
    Server server = new Server();

    @BeforeEach
    void setTestSettings() {
        server.writePort(8088);
        server.writeHost();
    }

    @Test
    void readPortEqualsSettings() {
        int expected = 8088;
        int actual = server.readPort();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readHostEqualsSettings() {
        String actual = server.readHost();
        String expected = "localhost";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readHostNotNull() {
        Assertions.assertNotNull(server.readHost());
    }
}