package com.github.igorcossta.utils;

import com.github.igorcossta.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

abstract class ConfigLoader {
    private File file = null;
    private FileConfiguration configuration;

    private final String fileName;
    private final String SUFFIX = ".yml";

    public void reloadConfiguration() {
        if (this.file == null)
            this.file = new File(Economy.instance.getDataFolder(), fileName + SUFFIX);

        this.configuration = YamlConfiguration.loadConfiguration(file);
        InputStream is = Economy.instance.getResource(fileName + SUFFIX);
        if (is != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
            this.configuration.setDefaults(yamlConfiguration);
        }
    }

    void saveConfiguration() {
        if (this.configuration == null || this.file == null)
            return;
        try {
            this.getConfiguration().save(this.file);
            Economy.instance.getLogger().info("Configuration '%s' was successfully saved.".formatted(fileName));
        } catch (IOException e) {
            Economy.instance.getLogger().severe("Failed to save configuration '%s'. Exception: %s"
                    .formatted(fileName, e.getMessage()));
        }
    }

    private void saveDefaults() {
        if (this.file == null)
            this.file = new File(Economy.instance.getDataFolder(), fileName + SUFFIX);
        if (!this.file.exists()) {
            Economy.instance.getLogger().info("Configuration '%s' not found on disk. Attempting to load defaults.".formatted(fileName));
            Economy.instance.saveResource(fileName + SUFFIX, false);
        }
    }

    FileConfiguration getConfiguration() {
        if (this.configuration == null) {
            Economy.instance.getLogger().info("Configuration '%s' is null. Reloading...".formatted(fileName));
            reloadConfiguration();
        }
        return this.configuration;
    }

    ConfigLoader(final String fileName) {
        this.fileName = fileName;
        saveDefaults();
    }
}
