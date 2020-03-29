package ru.karvozavr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Config {

    private static Properties properties;

    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static Object setProperty(String key, String value) {
        return properties.setProperty(key, value);
    }
}
