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

public class KillCountCommand  implements CommandExecutor {

    private UHC uhc;

    public KillCountCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if(arguments.length == 1) {
            Player target = Bukkit.getPlayer(arguments[0]);
            UHCPlayer uhcTarget = uhc.getPlayerManager().getUhcPlayers().get(target.getUniqueId());
            if(target != null) {
                player.sendMessage(uhc.getPREFIX() + ChatColor.DARK_AQUA + target.getName() + ChatColor.YELLOW + " has " + ChatColor.AQUA + uhcTarget.getKills() + ChatColor.YELLOW + " kills.");
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " could not be found");
            }
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /killcount <player>");
        }
        return false;
    }
}
