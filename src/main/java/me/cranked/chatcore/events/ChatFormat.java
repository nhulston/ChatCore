package me.cranked.chatcore.events;

import java.util.List;
import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.VersionManager;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check	
        if (!ConfigManager.getEnabled("custom-chat-format"))
            return;

        // Cancel check	
        if (e.isCancelled())
            return;

        // Vault check
        if (ChatCore.vaultChat == null)
            return;

        Player player = e.getPlayer();
        String format;
        try {
            format = Objects.requireNonNull(ChatCore.plugin.getConfig().getString("rank-formats." + ChatCore.vaultChat.getPrimaryGroup(player))).replace("%prefix%", ChatCore.vaultChat.getPlayerPrefix(player)).replace("%name%", player.getDisplayName()).replace("%suffix%", ChatCore.vaultChat.getPlayerSuffix(player)).replace("%message%", "");
        } catch (NullPointerException e2) {
            format = Objects.requireNonNull(ChatCore.plugin.getConfig().getString("default-format")).replace("%prefix%", ChatCore.vaultChat.getPlayerPrefix(player)).replace("%name%", player.getDisplayName()).replace("%suffix%", ChatCore.vaultChat.getPlayerSuffix(player)).replace("%message%", "");
        }
        format = ConfigManager.placeholderize(format, player);
        // Hover
        if (ConfigManager.getEnabled("name-hover")) {
            for (Player onlinePlayer : e.getRecipients()) {
                // Setup message
                format = PlaceholderAPI.setRelationalPlaceholders(player, onlinePlayer, format);
                BaseComponent[] component = TextComponent.fromLegacyText(format + e.getMessage());

                // Setup hovering
                List<String> list = ChatCore.plugin.getConfig().getStringList("hover-format"); // for some reason ConfigManager doesn't work here?
                for (int i = 0; i < list.size(); i++) {
                    String line = list.get(i).replace("%name%", player.getDisplayName()).replace("%prefix%", ChatCore.vaultChat.getPlayerPrefix(player));
                    line = PlaceholderAPI.setRelationalPlaceholders(player, onlinePlayer, line);
                    list.set(i, ConfigManager.placeholderize(line, player));
                }
                HoverEvent hoverEvent;
                if (VersionManager.isV16()) {
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(String.join("\n", list))));
                } else {
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(String.join("\n", list)).create()));
                }

                // Setup click
                String clickActionMode = ConfigManager.get("click-action-mode");
                assert clickActionMode != null;
                String action = ConfigManager.colorize(ConfigManager.get("click-action").replace("%name%", player.getName()));
                ClickEvent clickEvent;
                if (clickActionMode.equalsIgnoreCase("suggestcommand")) {
                    clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, action);
                } else if (clickActionMode.equalsIgnoreCase("runcommand")) {
                    clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, action);
                } else {
                    clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, action);
                }

                BaseComponent[] component2 = new ComponentBuilder()
                        .event(hoverEvent).event(clickEvent).
                        append(component).create();

                onlinePlayer.spigot().sendMessage(component2);
            }
            e.getRecipients().clear();
        } else {
            e.setFormat(format + e.getMessage());
        }
    }
}