package xyz.velium.uhc.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

public class ModeratorCommand implements CommandExecutor {

    private UHC uhc;

    public ModeratorCommand(UHC uhc) {
        this.uhc = uhc;
    }

    private void handleModerator(Player player) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if(uhcPlayer.getPlayerStates() != PlayerStates.MODERATOR) {
            uhcPlayer.setupPlayer();
            uhcPlayer.teleportSpawn();
            uhcPlayer.setPlayerStates(PlayerStates.MODERATOR);

            player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You are now in Mod Mode");

            uhc.getInventories().loadStaffHotBar(player);

            for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
                onlinePlayers.hidePlayer(player);
            }

            if(uhcPlayer.getTeam() != null) {
                Team team = uhcPlayer.getTeam();
                if(team.getLeader().equals(player.getUniqueId())) {
                    uhc.getTeamManager().deleteTeam(player);
                } else {
                    uhc.getTeamManager().leaveTeam(player);
                }
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setGameMode(GameMode.CREATIVE);
                }
            }.runTaskLater(uhc, 2);
        } else {
            if(uhc.getGameStates() == GameStates.PREPARE || uhc.getGameStates() == GameStates.LOBBY) {
                uhcPlayer.setupPlayer();
                uhcPlayer.setPlayerStates(PlayerStates.LOBBY);
                uhcPlayer.teleportSpawn();

                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You left the Mod Mode");

                uhc.getInventories().loadLobbyHotBar(player);

                for(Player onlinePlayers : uhc.getOnlinePlayers()) {
                    onlinePlayers.showPlayer(player);
                }
            } else if(uhc.getGameStates() == GameStates.SCATTER) {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t leave the Mod Mode while the Scatter is running");
            } else if(uhc.getGameStates() == GameStates.INGAME) {
                uhcPlayer.addSpectator();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(arguments.length == 0) {
                this.handleModerator(player);
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /moderator");
            }
        }
        return false;
    }
}
