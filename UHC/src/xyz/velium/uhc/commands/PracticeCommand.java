package xyz.velium.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class PracticeCommand implements CommandExecutor {

    private UHC uhc;

    public PracticeCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(arguments.length == 0) {
                UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
                if(uhc.getGameStates() == GameStates.LOBBY) {
                    if(uhc.getGameManager().isPractice()) {
                        uhcPlayer.handlePractice();
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Practice is currently disabled");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Practice is currently disabled");
                }
            } else if(arguments.length == 1) {
                if(arguments[0].equalsIgnoreCase("on")) {
                    if(player.hasPermission("uhc.host")) {
                        uhc.getGameManager().setPractice(true);
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.GREEN + "Practice has been enabled");
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only hosts can execute this command");
                    }
                } else if(arguments[0].equalsIgnoreCase("off")) {
                    if(player.hasPermission("uhc.host")) {
                        uhc.getGameManager().setPractice(false);
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.RED + "Practice has been disabled");
                        for(Player practicePlayers : uhc.getOnlinePlayers()) {
                            UHCPlayer uhcPractice = uhc.getPlayerManager().getUhcPlayers().get(practicePlayers.getUniqueId());
                            if(uhcPractice.getPlayerStates() == PlayerStates.PRACTICE) {
                                uhcPractice.handlePractice();
                            }
                        }
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only hosts can execute this command");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /practice");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /practice");
            }
        }
        return false;
    }
}
