package me.cranked.crankedcore;

import org.bukkit.plugin.java.JavaPlugin;

public final class CrankedCore extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("CrankedCore has started."); //TODO colors, etc.
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("CrankedCore has stopped.");
    }
}
