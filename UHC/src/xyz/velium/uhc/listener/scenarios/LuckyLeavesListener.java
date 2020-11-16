package xyz.velium.uhc.listener.scenarios;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.Scenarios;

public class LuckyLeavesListener implements Listener {

    private UHC uhc;

    public LuckyLeavesListener(UHC uhc) {
        this.uhc = uhc;
    }

    private double LUCKYLEAVES_RATE = 0.005;

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (!event.isCancelled()) {
            if(uhc.getGameStates() == GameStates.INGAME) {
                if (Scenarios.LUCKYLEAVES.isEnabled()) {
                    if (Math.random() <= LUCKYLEAVES_RATE) {
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLDEN_APPLE));
                    }
                }
            }
        }
    }
}
