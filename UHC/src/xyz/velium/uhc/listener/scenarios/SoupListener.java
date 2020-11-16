package xyz.velium.uhc.listener.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

public class SoupListener implements Listener {

    private UHC uhc;

    public SoupListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if (uhc.getGameStates() == GameStates.INGAME) {
            if (Scenarios.SOUP.isEnabled()) {
                if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (player.getItemInHand().getType() == Material.MUSHROOM_SOUP && player.getHealth() < 20.0D) {
                            double health = player.getHealth() + 7.0D;
                            if (health > 20.0D) {
                                health = 20.0D;
                            }
                            player.setHealth(health);
                            player.getItemInHand().setType(Material.BOWL);
                        }
                    }
                }
            }
        }
    }
}
