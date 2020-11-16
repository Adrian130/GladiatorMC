package xyz.velium.uhc.commands.team;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

public class BackPackCommand implements CommandExecutor {

    private UHC uhc;

    public BackPackCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if(arguments.length == 0) {
                if(Scenarios.BACKPACK.isEnabled()) {
                    if(uhc.getGameStates() == GameStates.INGAME) {
                        if(uhc.getTeamManager().isTeams()) {
                            if(uhcPlayer.getTeam() != null) {
                                Team team = uhcPlayer.getTeam();
                                player.openInventory(team.getBackPack());
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are not in a Team");
                            }
                        } else {
                            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Teams are currently disabled");
                        }
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "There is currently no game running");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "BackPacks are currently disabled");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /backpack");
            }
        }
        return false;
    }
}
