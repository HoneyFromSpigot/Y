package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LoggingService {
    private static Logger LOGGER;
    private File logFile;
    private FileHandler fileHandler;

    public LoggingService(){
        LOGGER = Logger.getLogger("Y-INTERNAL");
        try {
            File FOLDER = new File("./plugins/y/logs");

            if(FOLDER.exists()){
                System.out.println("Cleaning Folder!");
                FileUtils.deleteDirectory(FOLDER);
            }

            System.out.println("Making Folder");
            FOLDER.mkdirs();

            File logFile = new File(FOLDER, "log.txt");

            this.fileHandler = new FileHandler(logFile.getPath());
            LOGGER.addHandler(fileHandler);
            fileHandler.setFormatter(new YLogFormatter());

            LOGGER.info("LoggingService started!");
        } catch (Exception e){
            LOGGER.info("LoggingService failed to start!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(YPlugin.getInstance());
        }
    }

    public void close(){
        this.fileHandler.close();
    }

    public static void info(String message){
        LOGGER.info(message);
    }

    public static void warning(String message){
        LOGGER.warning(message);
    }

    public static void severe(String message){
        LOGGER.severe(message);
    }

    public static Logger get(){
        return LOGGER;
    }

    public class YLogFormatter extends Formatter {

        private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

        @Override
        public String format(final LogRecord record) {
            return String.format(
                    "%1$s %2$-7s %3$s\n",
                    new SimpleDateFormat(PATTERN).format(
                            new Date(record.getMillis())),
                    record.getLevel().getName(), formatMessage(record));
        }
    }
}
