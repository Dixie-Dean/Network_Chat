package settings;

import java.io.FileWriter;
import java.io.IOException;

public abstract class SettingsConfigurator extends SettingsHandler {
    private static final String HOST = "localhost";

    protected void writePort(int port) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Port | " + port + "\n");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    protected void writeHost() {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("Host | " + HOST + "\n");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
