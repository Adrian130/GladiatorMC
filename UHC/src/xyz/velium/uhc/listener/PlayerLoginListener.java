package xyz.velium.uhc.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.player.UHCPlayer;

public class PlayerLoginListener implements Listener {

    private UHC uhc;

    public PlayerLoginListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhc.getGameStates() == GameStates.PREPARE) {
            if(player.hasPermission("uhc.host")) {
                event.allow();
            } else {
                event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, uhc.getPREFIX() + ChatColor.RED + "the server is currently beeing prepared");
            }
        } else if(uhc.getGameStates() == GameStates.LOBBY) {
            if(uhc.getConfigManager().isWhitelist()) {
                if(player.hasPermission("uhc.donator") || uhc.getConfigManager().getWhitelistedPlayers().contains(player.getUniqueId())) {
                    event.allow();
                } else {
                    event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, uhc.getPREFIX() + ChatColor.RED + "the whitelist is currently enabled buy a rank at store.velium.xyz to bypass the whitelist");
                }
            }
        } else if(uhc.getGameStates() == GameStates.SCATTER) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, uhc.getPREFIX() + ChatColor.RED + "the scatter is currently running try again later");
        }
    }
}
