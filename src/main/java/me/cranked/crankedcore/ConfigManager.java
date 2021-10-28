package me.cranked.crankedcore;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private static File file;
    private static FileConfiguration customFile;

    public static void setup() {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("CrankedCore")).getDataFolder(), "deathmessages.yml");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Couldn't create file");
            }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
