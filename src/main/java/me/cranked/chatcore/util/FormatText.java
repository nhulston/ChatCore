package me.cranked.chatcore.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.VersionManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatText {
    private static final int CENTER_PX = 154;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public static final Pattern GRADIENT_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>(.*?)</#([A-Fa-f0-9]{6})>");
    private static final char COLOR_CHAR = org.bukkit.ChatColor.COLOR_CHAR;
    private static String centerText(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()){
            if (c == '§') {
                previousCode = true;
            } else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb + message;
    }

    /**
     * Colorizes messages
     * @param message The original message we want to colorize
     * @return A colorized String
     */
    private static String colorize(String message) {
        if (VersionManager.isV16()) {
            // Handle gradient
            Matcher gradientMatcher = GRADIENT_PATTERN.matcher(message);
            StringBuffer sb = new StringBuffer();
            while (gradientMatcher.find()) {
                String startColor = gradientMatcher.group(1);
                String endColor = gradientMatcher.group(3);
                String content = gradientMatcher.group(2);

                StringBuilder gradientText = new StringBuilder();
                String currentFormat = "";

                for (int i = 0; i < content.length(); i++) {
                    char c = content.charAt(i);

                    if (c == '&' && i < content.length() - 1) {
                        char next = content.charAt(i + 1);
                        switch (next) {
                            case 'l': currentFormat = "&l"; break;
                            case 'o': currentFormat = "&o"; break;
                            case 'n': currentFormat = "&n"; break;
                            case 'm': currentFormat = "&m"; break;
                            case 'k': currentFormat = "&k"; break;
                            case 'r': currentFormat = "&r"; break;
                        }
                        i++;  // Skip the next character
                        continue;
                    }

                    double ratio = (double) i / (content.length() - 1);
                    String interpolatedColor = interpolateColor(startColor, endColor, ratio);
                    gradientText.append(COLOR_CHAR).append("x");
                    for (char colorChar : interpolatedColor.toCharArray()) {
                        gradientText.append(COLOR_CHAR).append(colorChar);
                    }
                    if (!currentFormat.isEmpty()) {
                        gradientText.append(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', currentFormat));
                    }
                    gradientText.append(c);
                }

                gradientMatcher.appendReplacement(sb, gradientText.toString());
            }
            gradientMatcher.appendTail(sb);

            // Handle HEX
            Matcher hexMatcher = HEX_PATTERN.matcher(sb.toString());
            StringBuffer finalSb = new StringBuffer(sb.length() + 32);
            while (hexMatcher.find()) {
                String group = hexMatcher.group(1);
                hexMatcher.appendReplacement(finalSb, COLOR_CHAR + "x"
                        + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                        + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                        + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                );
            }
            return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', hexMatcher.appendTail(finalSb).toString());
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }


    /**
     * Colorizes components
     * @param message The original message we want to colorize
     * @return A colorized TextComponent
     */
    public static TextComponent colorizeTextComponent(String message) {
        TextComponent finalComponent = new TextComponent();

        Matcher gradientMatcher = GRADIENT_PATTERN.matcher(message);
        int lastEnd = 0;

        while (gradientMatcher.find()) {
            // Add any text before the gradient as a normal colored component
            if (gradientMatcher.start() > lastEnd) {
                String beforeText = message.substring(lastEnd, gradientMatcher.start());
                TextComponent beforeComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', beforeText));
                finalComponent.addExtra(beforeComponent);
            }

            // Handle gradient
            String startColor = gradientMatcher.group(1);
            String endColor = gradientMatcher.group(3);
            String content = gradientMatcher.group(2);

            boolean isBold = false, isItalic = false, isUnderlined = false, isStrikethrough = false, isObfuscated = false;

            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);

                if (c == '&' && i < content.length() - 1) {
                    char next = content.charAt(i + 1);
                    switch (next) {
                        case 'l': isBold = true; break;
                        case 'o': isItalic = true; break;
                        case 'n': isUnderlined = true; break;
                        case 'm': isStrikethrough = true; break;
                        case 'k': isObfuscated = true; break;
                        case 'r': isBold = isItalic = isUnderlined = isStrikethrough = isObfuscated = false; break;
                    }
                    i++;  // Skip the next character
                    continue;
                }

                double ratio = (double) i / (content.length() - 1);
                String interpolatedColor = "#" + interpolateColor(startColor, endColor, ratio);

                TextComponent component = new TextComponent(String.valueOf(c));
                component.setColor(net.md_5.bungee.api.ChatColor.of(interpolatedColor));
                component.setBold(isBold);
                component.setItalic(isItalic);
                component.setUnderlined(isUnderlined);
                component.setStrikethrough(isStrikethrough);
                component.setObfuscated(isObfuscated);
                finalComponent.addExtra(component);
            }

            lastEnd = gradientMatcher.end();
        }

        // Add any remaining text as a normal colored component
        if (lastEnd < message.length()) {
            String remainingText = message.substring(lastEnd);
            TextComponent remainingComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', remainingText));
            finalComponent.addExtra(remainingComponent);
        }

        return finalComponent;
    }

    public static String interpolateColor(String color1, String color2, double ratio) {
        int r1 = Integer.parseInt(color1.substring(0, 2), 16);
        int g1 = Integer.parseInt(color1.substring(2, 4), 16);
        int b1 = Integer.parseInt(color1.substring(4, 6), 16);

        int r2 = Integer.parseInt(color2.substring(0, 2), 16);
        int g2 = Integer.parseInt(color2.substring(2, 4), 16);
        int b2 = Integer.parseInt(color2.substring(4, 6), 16);

        int r = (int) (r1 * (1 - ratio) + r2 * ratio);
        int g = (int) (g1 * (1 - ratio) + g2 * ratio);
        int b = (int) (b1 * (1 - ratio) + b2 * ratio);

        return String.format("%02x%02x%02x", r, g, b);
    }

    /**
     * Colorizes messages and replaces placeholders
     * @param s The original message we want to update
     * @return A colorizes String with placeholders replaced
     */
    public static String placeholderize(String s, Player p) {
        return PlaceholderAPI.setPlaceholders(p, formatText(s));
    }

    public static String formatText(String message) {
        String[] messages = message.split("%newline%");
        for (int i = 0; i < messages.length; i++) {
            messages[i] = colorize(messages[i]);
            if (messages[i].contains("{center}")) {
                messages[i] = messages[i].replace("{center}", "");
                messages[i] = centerText(messages[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messages.length; i++) {
            sb.append(messages[i]);
            if (i < messages.length - 1) {
                sb.append("§r\n");
            }
        }
        return sb.toString();
    }


}