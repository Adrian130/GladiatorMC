package xyz.velium.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.player.UHCPlayer;

public class HealthCommand implements CommandExecutor {

    private UHC uhc;

    public HealthCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if(arguments.length == 1) {
            Player target = Bukkit.getPlayer(arguments[0]);
            if(target != null) {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + target.getName() + ChatColor.YELLOW + " is currently on " + ChatColor.DARK_RED.toString() + Math.ceil(((CraftPlayer)target).getHealth() / 2)  + "♥");
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " could not be found");
            }
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /health <player>");
        }
        return false;
    }
}
