package xyz.velium.uhc.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private UHC uhc;

    public PlayerJoinListener(UHC uhc) {
        this.uhc = uhc;
    }

    private void sendWelcomeMessage(Player player) {
        List<String> hosts = new ArrayList<>();
        List<String> moderators = new ArrayList<>();
        List<String> scenarioslist = new ArrayList<>();

        for(Player onlinePlayers : uhc.getOnlinePlayers()) {
            UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if(uhcPlayers.getPlayerStates() == PlayerStates.MODERATOR) {
                moderators.add(onlinePlayers.getName());
            } else if(uhcPlayers.getPlayerStates() == PlayerStates.HOST) {
                hosts.add(onlinePlayers.getName());
            }
        }

        for(Scenarios scenarios : Scenarios.values()) {
            if(scenarios.isEnabled()) {
                scenarioslist.add(scenarios.getScenarioName());
            }
        }

        String[] moderatorString = moderators.toArray(new String[moderators.size()]);
        String[] hostsString = hosts.toArray(new String[hosts.size()]);
        String[] scenarioString = scenarioslist.toArray(new String[scenarioslist.size()]);

        player.sendMessage(ChatColor.GOLD + "Welcome to Velium UHC");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + "Hosts: " + ChatColor.RED + Arrays.toString(hostsString).replace("[", "").replace("]", ""));
        player.sendMessage(ChatColor.YELLOW + "Moderators: " + ChatColor.DARK_AQUA + Arrays.toString(moderatorString).replace("[", "").replace("]", ""));
        player.sendMessage(ChatColor.YELLOW + "Scenarios: " + ChatColor.WHITE + Arrays.toString(scenarioString).replace("[", "").replace("]", ""));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);

        if(!uhc.getPlayerManager().getUhcPlayers().containsKey(player.getUniqueId())) {
            UHCPlayer uhcPlayer = new UHCPlayer(player.getUniqueId());
            uhc.getPlayerManager().getUhcPlayers().put(player.getUniqueId(), uhcPlayer);
        }

        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        uhcPlayer.hideSpectators();

        this.sendWelcomeMessage(player);

        if(uhc.getGameStates() == GameStates.LOBBY ||uhc.getGameStates() == GameStates.PREPARE) {
            uhcPlayer.setupPlayer();
            uhcPlayer.setPlayerStates(PlayerStates.LOBBY);
            uhcPlayer.teleportSpawn();

            uhc.getInventories().loadLobbyHotBar(player);
        } else if(uhc.getGameStates() == GameStates.INGAME) {

            if(uhcPlayer.getPlayerStates() != PlayerStates.INGAME) {
                uhcPlayer.addSpectator();
            } else if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                uhc.getPlayerManager().getLogoutPlayers().remove(player.getUniqueId());
                uhcPlayer.setLogoutTime(600);
                uhcPlayer.removeCombatLogger();
            }
        }
    }
}
