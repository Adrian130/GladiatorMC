package xyz.velium.uhc.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

public class PlayerQuitListener implements Listener {

    private UHC uhc;

    public PlayerQuitListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        event.setQuitMessage(null);

        if(uhc.getPlayerManager().getPracticePlayers().contains(player.getUniqueId())) {
            uhc.getPlayerManager().getPracticePlayers().remove(player.getUniqueId());
            uhcPlayer.setPracticeKills(0);
        }

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            if(uhcPlayer.getTeam() != null) {
                Team team = uhcPlayer.getTeam();
                if(team.getLeader().equals(uhcPlayer.getUuid())) {
                    uhc.getTeamManager().deleteTeam(player);
                } else {
                    uhc.getTeamManager().leaveTeam(player);
                }

                uhcPlayer.setTeam(null);
            }
        }
        if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
            uhcPlayer.spawnCombatVillager();
            uhcPlayer.savePlayer();
            uhc.getPlayerManager().getLogoutPlayers().add(player.getUniqueId());
        }
    }
}
