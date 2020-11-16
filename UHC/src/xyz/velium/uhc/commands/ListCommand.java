package xyz.velium.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ListCommand implements CommandExecutor {

    private UHC uhc;

    public ListCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(arguments.length == 0) {

                List<String> hosts = new ArrayList<>();
                List<String> moderators = new ArrayList<>();

                for(Player onlinePlayers : uhc.getOnlinePlayers()) {
                    UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
                    if(uhcPlayers.getPlayerStates() == PlayerStates.MODERATOR) {
                        moderators.add(onlinePlayers.getName());
                    } else if(uhcPlayers.getPlayerStates() == PlayerStates.HOST) {
                        hosts.add(onlinePlayers.getName());
                    }
                }

                String[] moderatorString = moderators.toArray(new String[moderators.size()]);
                String[] hostsString = hosts.toArray(new String[hosts.size()]);

                player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "UHC List");
                player.sendMessage(" ");
                player.sendMessage(ChatColor.YELLOW + "Hosts: " + ChatColor.RED + Arrays.toString(hostsString).replace("[", "").replace("]", ""));
                player.sendMessage(ChatColor.YELLOW + "Moderators: " + ChatColor.DARK_AQUA + Arrays.toString(moderatorString).replace("[", "").replace("]", ""));
                player.sendMessage(ChatColor.YELLOW + "Alive Players: " + ChatColor.WHITE + uhc.getPlayerManager().getAlivePlayers().size());
                player.sendMessage(ChatColor.YELLOW + "Online Players: " + ChatColor.WHITE + uhc.getOnlinePlayers().size());
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /list");
            }
        }
        return false;
    }
}
