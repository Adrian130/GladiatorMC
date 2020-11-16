package xyz.velium.uhc.listener.scenarios;

import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

public class HorselessListener implements Listener {

    private UHC uhc;

    public HorselessListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhc.getGameStates() == GameStates.INGAME) {
            if(Scenarios.HORSELESS.isEnabled()) {
                if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                    if(event.getRightClicked() instanceof Horse) {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Horses are disabled for this UHC");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
