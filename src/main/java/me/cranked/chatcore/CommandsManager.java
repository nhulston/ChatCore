package me.cranked.chatcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import me.cranked.chatcore.commands.*;
import me.cranked.chatcore.commands.api.ChatCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Manages all commands that start with "chat"
 * @author Nick
 * @since 1.0
 */
public class CommandsManager implements CommandExecutor {


    private final ArrayList<ChatCommand> subCommands = new ArrayList<>();

    public CommandsManager() {
        addSubCommands(
            new CommandAnnounce(),
            new CommandClear(),
            new CommandCommandSpy(),
            new CommandMute(),
            new CommandReload(),
            new CommandSlow(),
            new CommandStaffAnnounce(),
            new CommandStaffChat(),
            new CommandWarn()
        );
    }

    /**
     * Manages all commands that start with "chat"
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0 || subCommands.isEmpty()) {
            sendHelpMessage(sender);
            return true;
        }

        // Gets argument 0
        String arg = args[0];
        // Removes argument 0
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        Optional<ChatCommand> cmd = subCommands.stream().filter(chatCommand -> {
            if (!chatCommand.isEnabled())
                return false;

            if (chatCommand.getName().equalsIgnoreCase(arg))
                return true;

            List<String> aliases = chatCommand.getAliases();
            if (aliases == null || aliases.isEmpty())
                return false;

            return aliases.contains(arg.toLowerCase());
        }).findFirst();

        if (cmd.isPresent())
            runSubCommand(cmd.get(), sender, newArgs);
        else
            sendHelpMessage(sender);
        return true;


//        switch (args[0].toLowerCase()) {
//            case "help":
//                sendHelpMessage(sender);
//                break;
//            case "reload":
//                if (sender.hasPermission("chatcore.reload") || !(sender instanceof Player)) {
//                    ConfigManager.reload();
//                    DeathMessagesConfigManager.reload();
//                    sender.sendMessage(ConfigManager.get("reload"));
//                } else {
//                    sender.sendMessage(ConfigManager.get("no-permission"));
//                }
//                break;
//            case "clear":
//                Clear.command(sender, args);
//                break;
//            case "slow":
//                Slow.command(sender, args);
//                break;
//            case "lock":
//            case "mute":
//                Lock.command(sender, args);
//                break;
//            case "staff-chat":
//            case "staff":
//                StaffChat.command(sender, args);
//                break;
//            case "spy":
//                CommandSpy.command(sender);
//                break;
//            case "announce":
//            case "shout":
//            case "broadcast":
//                Announce.command(sender, args, "announce");
//                break;
//            case "warning":
//            case "warn":
//                Announce.command(sender, args, "warning");
//                break;
//            case "staffannounce":
//                Announce.command(sender, args, "staff-announce");
//                break;
//        }
//
//        return true;
    }

    private void runSubCommand(ChatCommand chatCommand, CommandSender sender, String[] args) {
        Optional<String> permission = Optional.ofNullable(chatCommand.getPermission());
        if (permission.isPresent() && !(sender.hasPermission(permission.get()))) {
            sender.sendMessage(ConfigManager.get("no-permission"));
            return;
        }

        boolean playerOnly = chatCommand.isPlayerOnly();
        boolean consoleOnly = chatCommand.isConsoleOnly();

        if (playerOnly && !(sender instanceof Player)) {
            // Send the player a "You cannot use this as a player" message, or something.
            return;
        }

        if (consoleOnly && sender instanceof Player) {
            // Send the console a "You cannot use this as the Console" message, or something.
            return;
        }

        chatCommand.onCommand(sender, args);
    }

    /**
     * Helper method for onCommand
     * Called when user runs "/chat" or "/chat help"
     * @param sender The user who is trying to get help
     */
    private void sendHelpMessage(CommandSender sender) {
        if (sender.hasPermission("chatcore.help")) {
            List<String> messages = ConfigManager.getList("help-msg");
            for (String msg : messages)
                sender.sendMessage(ConfigManager.colorize(msg));
        } else {
            TextComponent formatComponent = new TextComponent(ConfigManager.colorize("&aServer is running &b&nChatCore " + ChatCore.plugin.getDescription().getVersion() + "&a."));
            formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.mc-market.org/resources/13998/"));
            sender.spigot().sendMessage(formatComponent);
        }
    }

    public void addSubCommands(ChatCommand... commands) {
        this.subCommands.addAll(Arrays.asList(commands));
    }


}
