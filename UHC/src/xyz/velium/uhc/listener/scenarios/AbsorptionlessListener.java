package xyz.velium.uhc.listener.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

public class AbsorptionlessListener implements Listener {

    private UHC uhc;

    public AbsorptionlessListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
       if(uhc.getGameStates() == GameStates.INGAME) {
           if (Scenarios.ABSORPOTIONLESS.isEnabled() && event.getItem().getType() == Material.GOLDEN_APPLE) {
               if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                   player.removePotionEffect(PotionEffectType.ABSORPTION);

                   new BukkitRunnable() {
                       @Override
                       public void run() {
                           if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                               player.removePotionEffect(PotionEffectType.ABSORPTION);
                           }
                       }
                   }.runTaskLater(uhc, 1);
               }
           }
       }
    }
}
