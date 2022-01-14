package me.cranked.chatcore.commands.api;

import me.cranked.chatcore.commands.api.exception.InvalidCommandInfoException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ChatCommand {

    private final String name;
    private List<String> aliases;
    private String permission;
    private boolean playerOnly;
    private boolean consoleOnly;
    private boolean enabled;

    public ChatCommand() {
        if (!getClass().isAnnotationPresent(CommandInfo.class)) {
            throw new InvalidCommandInfoException();
        }

        CommandInfo annotation = getClass().getAnnotation(CommandInfo.class);
        this.name = annotation.name();

        if (annotation.consoleOnly())
            consoleOnly = true;

        if (annotation.playerOnly())
            playerOnly = true;

        List<String> a = new ArrayList<>(Arrays.asList(annotation.aliases()));
        // There will always be an empty index even if no arguments are
        // set. So the way you identify if there are actual arguments in the command
        // is you check if the first index is empty.
        if (!a.get(0).isEmpty()) {
            this.aliases = a;
        }

        if (!annotation.permission().isEmpty())
            this.permission = annotation.permission();
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public boolean isConsoleOnly() {
        return consoleOnly;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Utility Methods
    public Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
