package xyz.velium.uhc.commands.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;

public class LocationCommand implements CommandExecutor {

    private UHC uhc;

    public LocationCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(arguments.length == 1) {
                uhc.getLocationManager().setLocation(arguments[0], player.getLocation());
                player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You successfully added the Location: " + ChatColor.YELLOW + arguments[0]);
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /setlocation <name>");
            }
        }
        return false;
    }
}
