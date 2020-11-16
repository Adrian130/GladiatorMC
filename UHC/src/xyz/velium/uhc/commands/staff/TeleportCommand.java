package xyz.velium.uhc.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class TeleportCommand implements CommandExecutor {

    private UHC uhc;

    public TeleportCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if(arguments.length == 1) {
                if(uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
                    Player target = Bukkit.getPlayer(arguments[0]);
                    if(target != null) {
                        player.teleport(target.getLocation());
                        player.sendMessage(uhc.getPREFIX() + ChatColor.YELLOW + "You have been teleported to " + ChatColor.DARK_AQUA + target.getName());
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " could not be found");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only hosts can execute this command");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /teleport <player>");
            }
        }
        return false;
    }
}
