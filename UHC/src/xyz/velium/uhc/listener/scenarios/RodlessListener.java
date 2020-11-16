package xyz.velium.uhc.listener.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

public class RodlessListener implements Listener {

    private UHC uhc;

    public RodlessListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhc.getGameStates() == GameStates.INGAME) {
            if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                if(Scenarios.RODLESS.isEnabled()) {
                    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.getPlayer().getItemInHand().getType() == Material.FISHING_ROD) {
                        event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
