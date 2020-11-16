package xyz.velium.uhc.commands.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

import java.util.UUID;

public class TeamListCommand implements CommandExecutor {

    private UHC uhc;

    public TeamListCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(uhc.getTeamManager().isTeams()) {
                if(arguments.length == 0) {
                    UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
                    if(uhcPlayer.getTeam() != null) {
                        Team team = uhcPlayer.getTeam();
                        player.sendMessage(ChatColor.YELLOW + "Members of Team #" + uhcPlayer.getTeam().getTeamNumber());
                        for(UUID teamMembers : team.getPlayers()) {
                            OfflinePlayer teamPlayers = Bukkit.getPlayer(teamMembers);
                            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + teamPlayers.getName());
                        }
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are not in a Team");
                    }
                } else if(arguments.length == 1) {
                    Player target = Bukkit.getPlayer(arguments[0]);
                    UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(target.getUniqueId());
                    if(target != null) {
                        if(uhcPlayer.getTeam() != null) {
                            Team team = uhcPlayer.getTeam();
                            player.sendMessage(ChatColor.YELLOW + "Members of Team #" + uhcPlayer.getTeam().getTeamNumber());
                            for(UUID teamMembers : team.getPlayers()) {
                                OfflinePlayer teamPlayers = Bukkit.getPlayer(teamMembers);
                                player.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + teamPlayers.getName());
                            }
                        } else {
                            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " is not in a Team");
                        }
                    } else {
                        player.sendMessage(uhc.getPREFIX() + target.getName() + ChatColor.RED + " could not be found");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /teamlist <player>");
                }
            }
        }
        return false;
    }
}
