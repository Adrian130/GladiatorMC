package xyz.velium.uhc.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

import java.util.UUID;

public class DebugCommand implements CommandExecutor {

    private UHC uhc;

    public DebugCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if (uhcPlayer.getPlayerStates() == PlayerStates.HOST || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || player.isOp()) {
                if (uhc.getTeamManager().isTeams()) {
                    if (arguments[0].equalsIgnoreCase("teamlist")) {
                        if (arguments.length == 2) {
                            OfflinePlayer target = Bukkit.getPlayer(arguments[1]);
                            if (uhc.getPlayerManager().getUhcPlayers().containsKey(target.getUniqueId())) {
                                UHCPlayer uhcTarget = uhc.getPlayerManager().getUhcPlayers().get(target.getUniqueId());
                                if (uhcTarget.getTeam() != null) {
                                    Team team = uhcTarget.getTeam();

                                    player.sendMessage(uhc.getPREFIX() + ChatColor.YELLOW + "Alive Players of Team: #" + team.getTeamNumber());
                                    for (UUID uuid : team.getAlivePlayers()) {
                                        OfflinePlayer teamPlayers = Bukkit.getPlayer(uuid);

                                        player.sendMessage(ChatColor.WHITE + "- " + teamPlayers.getName());
                                    }
                                } else {
                                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " is not in a Team");
                                }
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " hasn´t played the UHC");
                            }
                        } else {
                            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /debug teamlist <player>");
                        }
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /debug teamlist <player>");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Teams are currently disabled");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only Hosts can execute this command");
            }
        }
        return false;
    }
}
