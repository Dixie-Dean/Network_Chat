package settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public abstract class SettingsHandler {
    protected static final String FILE_NAME = "src/main/java/info/settings.txt";
    protected static final String SETTINGS = "/settings";

    protected static int readPort() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String content;
            while ((content = reader.readLine()) != null) {
                String[] pair = content.split(Pattern.quote(" | "));
                String key = pair[0];
                int value = Integer.parseInt(pair[1]);

                if (key.equals("Port")) {
                    return value;
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return -1;
    }

    protected static String readHost() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String content;
            while ((content = reader.readLine()) != null) {
                String[] pair = content.split(Pattern.quote(" | "));
                String key = pair[0];
                String value = pair[1];

                if (key.equals("Host")) {
                    return value;
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    protected static void displaySettings() {
        System.out.printf("Port: %d, Host: %s", readPort(), readHost());
    }
}
