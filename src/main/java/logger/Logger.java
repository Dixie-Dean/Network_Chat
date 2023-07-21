package logger;

import java.io.*;

public class Logger {
    private static final String logFile = "src/main/java/info/ServerInfo.txt";
    private static Logger logger;
    private Logger() {}

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void log(String messageToLog) {
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true))) {
            logWriter.write(messageToLog);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
