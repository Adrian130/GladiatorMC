package xyz.velium.uhc.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

public class CombatLoggerTask {

    private final UHC uhc;
    private int combatLoggerTask;

    public CombatLoggerTask(UHC uhc) {
        this.uhc = uhc;
    }

    public void startCombatLoggerTask() {
        combatLoggerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, new BukkitRunnable() {
            @Override
            public void run() {
                if (uhc.getPlayerManager().getLogoutPlayers().size() >= 1) {
                    for (OfflinePlayer offlinePlayers : Bukkit.getOfflinePlayers()) {
                        if (uhc.getPlayerManager().getLogoutPlayers().contains(offlinePlayers.getUniqueId())) {
                            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(offlinePlayers.getUniqueId());
                            if (uhcPlayer.getPlayerStates() == PlayerStates.INGAME && uhc.getPlayerManager().getAlivePlayers().contains(uhcPlayer.getUuid())) {
                                uhcPlayer.setLogoutTime(uhcPlayer.getLogoutTime() - 1);

                                if (uhcPlayer.getLogoutTime() == 0) {
                                    uhcPlayer.dropPlayerDeathInventory();
                                    Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getOfflinePlayer(uhcPlayer.getUuid()).getName() + ChatColor.GRAY + "[" + ChatColor.WHITE + uhcPlayer.getKills() + ChatColor.GRAY + "] " + ChatColor.YELLOW + "died " + ChatColor.GRAY + "(Combat Logger)");

                                    uhc.getPlayerManager().getAlivePlayers().remove(uhcPlayer.getUuid());
                                    uhc.getPlayerManager().getLogoutPlayers().remove(uhcPlayer.getUuid());

                                    if (uhc.getTeamManager().isTeams()) {
                                        if (uhcPlayer.getTeam() != null) {
                                            Team team = uhcPlayer.getTeam();

                                            team.getAlivePlayers().remove(uhcPlayer.getUuid());

                                            if (team.getAlivePlayers().size() == 0) {
                                                uhc.getTeamManager().getAliveTeams().remove(team);
                                            }
                                        }
                                    }
                                    uhc.getGameManager().checkWin();
                                }
                            }
                        }
                    }
                }
            }
        }, 20, 20);
    }
}
