package xyz.velium.uhc.listener;

import me.uhc.worldborder.Events.WorldBorderFillFinishedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;

public class WorldBorderFillFinishListener implements Listener {

    private UHC uhc;

    public WorldBorderFillFinishListener(UHC uhc) {
        this.uhc = uhc;
    }
    
    @EventHandler
    public void onWorldBorderFillFinish(WorldBorderFillFinishedEvent event) {
        World world = event.getWorld();

        if (world.getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_world())) {

            uhc.getConfigManager().setReset(false);

            Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.GREEN + ChatColor.BOLD.toString() + "UHC World has finished loading");
            Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.GREEN + ChatColor.BOLD.toString() + "The Server will restart in 20 seconds");

            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.shutdown();
                }
            }.runTaskLater(uhc, 20 * 10);
        }
    }
}
