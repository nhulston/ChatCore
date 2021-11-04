package me.cranked.chatcore.events;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockedWords implements Listener {
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        // Config check
        if (ConfigManager.getList("blocked-words").size() == 0)
            return;
        
        // Bypass check
        if (e.getPlayer().hasPermission("chatcore.blockedwords.bypass"))
            return;
        
        
        String msg = e.getMessage();
        final String originalMsg = msg;
        List<String> blockedWordsNotIgnore = ConfigManager.getList("blocked-words");
        List<String> blockedWords = ConfigManager.getList("blocked-words-ignore-in-bigger-words");
        blockedWords.addAll(blockedWordsNotIgnore);
        for (String word : blockedWords) {
            // Calculate word based on punishment or not
            final String blockedWord;
            int commaLoc = word.indexOf(",");
            if (commaLoc != -1) {
                blockedWord = word.substring(0, commaLoc);
            } else {
                blockedWord = word;
            }

            // Checking if full word, not a part of the word
            StringBuilder stringPattern = new StringBuilder("(?i)\\b(");
            for (int i = 0; i < blockedWord.length(); i++)
                stringPattern.append(blockedWord.charAt(i)).append("+(\\W|\\d|_)*");
            stringPattern.append(")");
            Pattern pattern = Pattern.compile(stringPattern.toString());
            Matcher matcher = pattern.matcher(msg);
            String msgBefore = msg;
            msg = matcher.replaceAll(ConfigManager.get("blocked-words-replace-word"));

            // Punish
            if (!msg.equals(msgBefore) && word.contains(",")) {
                punish(word, commaLoc, e, blockedWord, originalMsg);
            }
        }

        // Checking if contains
        for (String word : blockedWordsNotIgnore) {
            final String blockedWord;
            int commaLoc = word.indexOf(",");
            if (commaLoc != -1) {
                blockedWord = word.substring(0, commaLoc);
            } else {
                blockedWord = word;
            }
            String msgBefore = msg;
            if (msg.toLowerCase().contains(blockedWord)) {
                int loc = msg.toLowerCase().indexOf(blockedWord);
                StringBuilder replaceWord = new StringBuilder();
                for (int i = 0; i < blockedWord.length(); i++)
                    replaceWord.append(ConfigManager.get("blocked-words-replace-char"));
                msg = msg.substring(0, loc) + replaceWord + msg.substring(loc + blockedWord.length());
            }
            if (!msg.equals(msgBefore) && word.contains(",")) {
                punish(word, commaLoc, e, blockedWord, originalMsg);
            }
        }
        e.setMessage(msg);
    }

    private void punish(String word, int commaLoc, final AsyncPlayerChatEvent e, String blockedWord, String originalMsg) {
        String punishmentCategory = word.substring(commaLoc + 2);
        List<String> punishments = ChatCore.plugin.getConfig().getStringList("blocked-words-punishments." + punishmentCategory);
        for (String punishment : punishments) {
            if (e.isAsynchronous()) {
                (new BukkitRunnable() {
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.placeholderize(punishment.replaceAll("%player%", e.getPlayer().getName()).replaceAll("%word%", blockedWord).replaceAll("%message%", originalMsg), e.getPlayer()));
                    }
                }).runTask(ChatCore.plugin);
                continue;
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.placeholderize(punishment.replaceAll("%player%", e.getPlayer().getName()).replaceAll("%word%", blockedWord).replaceAll("%message%", originalMsg), e.getPlayer()));
        }
    }
}
