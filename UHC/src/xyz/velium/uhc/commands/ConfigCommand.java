package xyz.velium.uhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;

public class ConfigCommand implements CommandExecutor {

    private UHC uhc;

    public ConfigCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (arguments.length == 0) {
                uhc.getInventories().openConfigInventory(player);
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /config");
            }
        }
        return false;
    }
}
