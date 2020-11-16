package xyz.velium.uhc.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class RespawnCommand implements CommandExecutor {

    private UHC uhc;

    public RespawnCommand(UHC uhc) {
        this.uhc = uhc;
    }

    private void respawnPlayer(Player player, Player staff) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        uhcPlayer.respawnPlayer();

        player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You have been respawned");
        staff.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + player.getName() + " has been respawned");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if(uhcPlayer.getPlayerStates() == PlayerStates.HOST || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || player.isOp()) {
                if(arguments.length == 1) {
                    if(uhc.getGameStates() == GameStates.INGAME) {
                        Player target = Bukkit.getPlayer(arguments[0]);
                        UHCPlayer uhcTarget = uhc.getPlayerManager().getUhcPlayers().get(target.getUniqueId());
                        if(target != null) {
                            if(uhcTarget.getPlayerStates() != PlayerStates.INGAME) {
                                if(!uhc.getPlayerManager().getAlivePlayers().contains(target.getUniqueId())) {
                                    if(uhcTarget.isHasplayed()) {
                                        this.respawnPlayer(target, player);
                                    } else {
                                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " has never played this UHC");
                                    }
                                } else {
                                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " is alive");
                                }
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " is alive");
                            }
                        } else {
                            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[0] + " could not be found");
                        }
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "There is currently no game running");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /respawn <player>");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only Hosts can execute this command");
            }
        }
        return false;
    }
}
