package xyz.velium.uhc.listener.scenarios;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

public class TimberListener implements Listener {

    private UHC uhc;

    public TimberListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if(uhc.getGameStates() == GameStates.INGAME) {
            if(Scenarios.TIMBER.isEnabled()) {
                if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                    if (event.isCancelled()) return;

                    if (event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2) {
                        Block up = event.getBlock();
                        while (up.getType() == Material.LOG || up.getType() == Material.LOG_2) {
                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(up.getType(), 1, up.getData()));
                            up.setType(Material.AIR);
                            up = up.getLocation().clone().add(0, 1, 0).getBlock();
                        }
                    }
                }
            }
        }
    }
}