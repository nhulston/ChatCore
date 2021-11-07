package me.cranked.chatcore.events;

import java.util.List;
import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.VersionManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check	
        if (!ConfigManager.getEnabled("custom-chat-format"))
            return;
        // Cancel check	
        if (e.isCancelled())
            return;
        // Vault chat	
        if (ChatCore.vaultChat != null) {
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
                    List<String> list = ChatCore.plugin.getConfig().getStringList("hover-format"); // for some reason ConfigManager doesn't work here?
                    TextComponent formatComponent = new TextComponent(PlaceholderAPI.setRelationalPlaceholders(player, onlinePlayer, format));
                    for (int i = 0; i < list.size(); i++) {
                        String line = list.get(i).replace("%name%", player.getDisplayName()).replace("%prefix%", ChatCore.vaultChat.getPlayerPrefix(player));
                        line = PlaceholderAPI.setRelationalPlaceholders(player, onlinePlayer, line);
                        list.set(i, ConfigManager.placeholderize(line, player));
                    }
                    if (VersionManager.isV16()) {
                        formatComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(String.join("\n", list)))));
                    } else {
                        formatComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(String.join("\n", list)).create())));
                    }
                    String clickActionMode = ConfigManager.get("click-action-mode");
                    assert clickActionMode != null;
                    String action = ConfigManager.colorize(ConfigManager.get("click-action").replace("%name%", player.getName()));
                    if (clickActionMode.equalsIgnoreCase("suggestcommand")) {
                        formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, action));
                    } else if (clickActionMode.equalsIgnoreCase("runcommand")) {
                        formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, action));
                    } else if (clickActionMode.equalsIgnoreCase("url")) {
                        formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, action));
                    }
                    onlinePlayer.spigot().sendMessage(formatComponent, new TextComponent(format.substring(format.length() - 2) + e.getMessage()));
                }
                e.setCancelled(true);
            } else {
                e.setFormat(format + e.getMessage());
            }
        }
    }
}