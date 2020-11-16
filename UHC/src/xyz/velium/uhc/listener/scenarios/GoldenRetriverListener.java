package xyz.velium.uhc.listener.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

public class GoldenRetriverListener implements Listener {

    private UHC uhc;

    public GoldenRetriverListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhc.getGameStates() == GameStates.INGAME) {
            if(Scenarios.GOLDENRETRIEVER.isEnabled()) {
                if (uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                    Bukkit.getWorld(uhc.getWorldManager().getUhc_world()).dropItemNaturally(player.getLocation(), new ItemStack(Material.GOLDEN_APPLE));
                }
            }
        }
    }
}
