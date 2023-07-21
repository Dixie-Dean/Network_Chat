package logger;

import java.io.*;

public class Logger {
    private final String path;

    public Logger(String path) {
        this.path = path;
    }

    public void log(String messageToLog) {
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(path, true))) {
            logWriter.write(messageToLog);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
