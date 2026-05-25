package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final ThreadLocal<Properties> props = new ThreadLocal<>();

    public static void loadConfig(String env) {
        Properties properties = new Properties();

        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Warning: application.properties not found.");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            System.out.println("Error loading application.properties: " + ex.getMessage());
        }

        String envFileName = env + ".properties";
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(envFileName)) {
            if (input == null) {
                throw new RuntimeException("ENVIRONMENT FILE IS MISSING: " + envFileName);
            }
            properties.load(input);
            System.out.println("Loaded environment configuration: " + envFileName);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading " + envFileName, ex);
        }

        props.set(properties);
    }

    public static String get(String key) {
        if (props.get() == null) {
            throw new RuntimeException("Configuration not loaded.");
        }
        return props.get().getProperty(key);
    }

    public static void clear() {
        props.remove();
    }
}
