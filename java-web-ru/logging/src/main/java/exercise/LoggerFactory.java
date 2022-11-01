package exercise;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerFactory {
    // Путь до файла конфигурации логгера
    private static final String LOGGER_CONFIG_FILE_PATH = "logging.properties";

    // Метод для получения экземпляра логгера
    public static Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz.getName());
    }

    // Метод для инициализации логгера
    public static void initLogger() {
        try {
            // Загружаем конфигурацию из файла
            FileInputStream fileInputStream = new FileInputStream(LOGGER_CONFIG_FILE_PATH);
            // Конфигурируем логгер
            LogManager.getLogManager().readConfiguration(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
