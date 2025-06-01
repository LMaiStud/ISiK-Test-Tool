package com.isik.testsuit.config;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ConfigLoader {

    private static ConfigLoader instance;
    private static final Object LOCK = new Object();
    private String CONFIG_FILE_PATH = "src/main/resources/config";
    private Properties properties;

    /**
     * Privater Konstruktor zum Laden der Konfiguration.
     */
    private ConfigLoader() {
        this.properties = new Properties();
        loadConfig();
    }

    /**
     * Liefert die Singleton-Instanz von ConfigLoader.
     */
    public static ConfigLoader getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ConfigLoader();
                }
            }
        }
        return instance;
    }

    /**
     * Lädt die Konfiguration aus der Datei.
     */
    private void loadConfig() {
        try (InputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return properties.getProperty("baseUrl");
    }

    public String getEndPointISiK() {
        return properties.getProperty("endPointISiK");
    }

    public String getEndPointLogin() {
        return properties.getProperty("endPointLogin");
    }

    public String getUsername() {
        return properties.getProperty("username");
    }

    public String getPwd() {
        return properties.getProperty("pwd");
    }

    public String getPort() {
        return properties.getProperty("port");
    }

    public String getHash() {
        return properties.getProperty("hash");
    }
}
