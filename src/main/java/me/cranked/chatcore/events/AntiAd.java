package me.cranked.chatcore.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.util.FormatText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AntiAd implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        // Config check
        if (!ConfigManager.getEnabled("anti-ad"))
            return;

        // Permission check
        Player player = e.getPlayer();
        if (player.hasPermission("chatcore.antiad.bypass"))
            return;

        // Check if contains whitelist
        for (String allowed : ConfigManager.getList("anti-ad-whitelist")) {
            if (e.getMessage().toLowerCase().contains(allowed.toLowerCase()))
                return;
        }

        Pattern pattern = Pattern.compile("\\b[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}\\b");
        Pattern pattern2 = Pattern.compile("\\b[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,)+[0-9]{1,3}\\b");
        Pattern pattern3 = Pattern.compile("[a-zA-Z0-9\\-.]+\\s?(\\.|dot|\\(dot\\)|-|;|:|,)\\s?(c(| +)o(| +)m|o(| +)r(| +)g|n(| +)e(| +)t|c(| +)z|c(| +)o|u(| +)k|s(| +)k|b(| +)i(| +)z|m(| +)o(| +)b(| +)i|x(| +)x(| +)x|e(| +)u|m(| +)e|i(| +)o)\\b");
        Pattern pattern4 = Pattern.compile("[a-zA-Z0-9\\-.]+\\s?(\\.|dot|\\(dot\\)|;|:|,)\\s?(com|org|net|cz|co|uk|sk|biz|mobi|xxx|eu|io)\\b");
        String msg = e.getMessage();
        Matcher matcher = pattern.matcher(msg);
        Matcher matcher2 = pattern2.matcher(msg);
        Matcher matcher3 = pattern3.matcher(msg);
        Matcher matcher4 = pattern4.matcher(msg);
        if (matcher.find() || matcher2.find() || matcher3.find() || matcher4.find()) {
            if (ConfigManager.getInt("anti-ad-setting") == 1) {
                e.setCancelled(true);
            } else {
                e.setMessage(msg.replaceAll("\\.", " "));
            }

            // Send warning message
            player.sendMessage(ConfigManager.get("anti-ad"));

            // Broadcast to staff
            String warningMsg = FormatText.formatText(ConfigManager.get("anti-ad-inform").replace("%player%", player.getName()).replace("%message%", e.getMessage()));
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("chatcore.antiad.alert")) {
                    p.sendMessage(warningMsg);
                }
            }
        }
    }
}
