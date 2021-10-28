package me.cranked.crankedcore.events;

import java.util.List;
import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.crankedcore.CrankedCore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {
    private final CrankedCore plugin;

    public ChatFormat(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!plugin.getConfig().getBoolean("custom-chat-format-enabled"))
            return;

        // Cancel check
        if (e.isCancelled())
            return;

        // Vault chat
        if (CrankedCore.vaultChat != null) {
            Player player = e.getPlayer();
            String format;
            try {
                format = ((String)Objects.requireNonNull(plugin.getConfig().getString("rank-formats." + CrankedCore.vaultChat.getPrimaryGroup(player)))).replace("%prefix%", CrankedCore.vaultChat.getPlayerPrefix(player)).replace("%name%", player.getDisplayName()).replace("%suffix%", CrankedCore.vaultChat.getPlayerSuffix(player)).replace("%message%", "");
            } catch (NullPointerException e2) {
                format = ((String)Objects.requireNonNull(plugin.getConfig().getString("default-format"))).replace("%prefix%", CrankedCore.vaultChat.getPlayerPrefix(player)).replace("%name%", player.getDisplayName()).replace("%suffix%", CrankedCore.vaultChat.getPlayerSuffix(player)).replace("%message%", "");
            }
            format = CrankedCore.placeholderColor(format, player);

            // Hover TODO optimize
            if (plugin.getConfig().getBoolean("name-hover-enabled")) {
                for (Player onlinePlayer : e.getRecipients()) {
                    TextComponent formatComponent = new TextComponent(PlaceholderAPI.setRelationalPlaceholders(player, onlinePlayer, format));
                    List<String> list = plugin.getConfig().getStringList("hover-format");
                    for (int i = 0; i < list.size(); i++) {
                        String line = list.get(i).replace("%name%", player.getDisplayName()).replace("%prefix%", CrankedCore.vaultChat.getPlayerPrefix(player));
                        line = PlaceholderAPI.setRelationalPlaceholders(player, onlinePlayer, line);
                        list.set(i, CrankedCore.placeholderColor(line, player));
                    }
                    formatComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(String.join("\n", list))).create()));
                    String clickAction = plugin.getConfig().getString("click-action-mode");
                    assert clickAction != null;
                    if (clickAction.equalsIgnoreCase("suggestcommand")) {
                        formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Objects.requireNonNull(plugin.getConfig().getString("click-action")).replace("%name%", player.getName())));
                    } else if (clickAction.equalsIgnoreCase("runcommand")) {
                        formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Objects.requireNonNull(plugin.getConfig().getString("click-action")).replace("%name%", player.getName())));
                    } else if (clickAction.equalsIgnoreCase("url")) {
                        formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Objects.requireNonNull(plugin.getConfig().getString("click-action")).replace("%name%", player.getName())));
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
