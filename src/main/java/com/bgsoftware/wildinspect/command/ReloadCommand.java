package com.bgsoftware.wildinspect.command;

import com.bgsoftware.wildinspect.Locale;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.handlers.SettingsHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public final class ReloadCommand implements CommandExecutor, TabCompleter {

    private static final WildInspectPlugin plugin = WildInspectPlugin.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("wildinspect.reload")) {
            Locale.NO_PERMISSION.send(sender);
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            new Thread(() -> {
                SettingsHandler.reload();
                Locale.reload();
                plugin.getCoreProtect().refreshProvider();
                Locale.RELOAD_SUCCESS.send(sender);
            }).start();
            return false;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /wildinspect reload");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }
}
