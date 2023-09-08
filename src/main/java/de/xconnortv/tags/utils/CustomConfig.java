package de.xconnortv.tags.utils;

import de.xconnortv.tags.Tags;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class CustomConfig {
    private FileConfiguration newConfig;
    private final String configName;
    private final Tags instance;
    private final File configFile;

    public CustomConfig(String configName, Tags instance) {
        this.configName = configName;
        this.instance = instance;
        this.configFile = new File(instance.getDataFolder(), configName);
    }

    public FileConfiguration get() {
        if(this.newConfig == null) {
            this.reloadConfig();
        }

        return this.newConfig;
    }

    public void reloadConfig() {
        this.newConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = this.instance.getResource(this.configName);
        if(defConfigStream != null) {
            this.newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
        }
    }

    public void saveConfig() {
        try {
            this.get().save(this.configFile);
        } catch (IOException var2) {
            this.instance.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, var2);
        }
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.instance.saveResource(this.configName, false);
        }

    }

    public void copyDefault() {
        if(!this.configFile.exists()) {
            get().options().copyDefaults(true);
            saveConfig();
        }
    }

}
