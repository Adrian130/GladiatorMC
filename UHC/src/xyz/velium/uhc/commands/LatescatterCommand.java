package xyz.velium.uhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.player.UHCPlayer;

public class LatescatterCommand implements CommandExecutor {

    private UHC uhc;

    public LatescatterCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if (arguments.length == 0) {
                if(uhc.getGameStates() == GameStates.INGAME) {
                    if(!uhcPlayer.isHasplayed()) {
                        if(!uhc.getConfigManager().isPvp()) {
                            uhcPlayer.lateScatter();
                        } else {
                            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t latescatter anymore");
                        }
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You already played this game");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "There is currently no game running");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /latescatter");
            }
        }
        return false;
    }
}
