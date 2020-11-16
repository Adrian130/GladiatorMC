package xyz.velium.uhc.listener.scenarios;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.util.ItemStackBuilder;

public class TimeBombListener implements Listener {

    private UHC uhc;

    public TimeBombListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhc.getGameStates() == GameStates.INGAME) {
            if(Scenarios.TIMEBOMB.isEnabled()) {
                if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                    event.getDrops().clear();
                    new TimeBomb(player.getName(), player.getLocation(), player.getLevel() * 10, player.getInventory().getContents(), player.getInventory().getArmorContents(), uhc);
                    return;
                }
            }
        }
    }

    public class TimeBomb {

        public TimeBomb(String name, Location location, int xp, ItemStack[] inv, ItemStack[] armor, UHC uhc) {
            location.getWorld().spawn(location, ExperienceOrb.class).setExperience(xp);

            Block chest1 = location.getBlock();
            chest1.setType(Material.CHEST);
            Block chest2 = chest1.getRelative(BlockFace.NORTH);
            chest2.setType(Material.CHEST);

            chest1.getRelative(BlockFace.UP).setType(Material.AIR);
            chest2.getRelative(BlockFace.UP).setType(Material.AIR);

            final Chest chest = (Chest) chest1.getState();
            for (ItemStack stack : armor) {
                if ((stack != null) && (stack.getType() != Material.AIR)) {
                    chest.getInventory().addItem(stack);
                }
            }

            chest.getInventory().addItem(new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE).setName(ChatColor.GOLD + "Golden Head").build());

            for (ItemStack stack : inv) {
                if ((stack != null) && (stack.getType() != Material.AIR)) {
                    chest.getInventory().addItem(stack);
                }
            }

            Hologram hologram = HologramAPI.createHologram(chest.getLocation().clone().add(0.5, 1, 0), ChatColor.GREEN + "30");
            hologram.spawn();

            Hologram above = hologram.addLineAbove(ChatColor.GOLD + name + "'s Timebomb");


            new BukkitRunnable() {
                //spawn hologram
                int duration = 30;

                String prefix = ChatColor.GRAY + ChatColor.BOLD.toString() + "(" + ChatColor.RED + ChatColor.BOLD.toString() +
                        "Timebomb" + ChatColor.GRAY + ChatColor.BOLD.toString() + ") " + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "» " + ChatColor.RESET;

                public void run() {
                    if (duration == 0) {
                        if (hologram.isSpawned()) hologram.despawn();
                        if (above.isSpawned()) above.despawn();
                        if (chest.getLocation().getBlock().getType() == Material.CHEST) chest.getInventory().clear();
                        location.getWorld().createExplosion(location, 10.0F);
                        this.cancel();

                        Bukkit.broadcastMessage(prefix + ChatColor.RED + name + ChatColor.YELLOW + "´s corpse has exploded!");
                        return;
                    }

                    duration--;

                    if (duration > 20) {
                        hologram.setText(ChatColor.GREEN.toString() + duration);
                    } else if (duration > 10) {
                        hologram.setText(ChatColor.GOLD.toString() + duration);
                    } else {
                        hologram.setText(ChatColor.RED.toString() + duration);
                    }
                }
            }.runTaskTimer(uhc, 20, 20);
        }
    }
}
