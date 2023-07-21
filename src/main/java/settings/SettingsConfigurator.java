package settings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public abstract class SettingsConfigurator extends SettingsHandler {
    private static final String HOST = "localhost";

    protected void configureSettings(Scanner scanner) {
        System.out.print("Enter port number: ");
        writePort(Integer.parseInt(scanner.nextLine()));
        writeHost();
    }

    private void writePort(int port) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("Port | " + port + "\n");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void writeHost() {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write("Host | " + HOST + "\n");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
