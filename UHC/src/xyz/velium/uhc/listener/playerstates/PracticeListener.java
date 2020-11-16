package xyz.velium.uhc.listener.playerstates;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.util.ItemStackBuilder;

public class PracticeListener implements Listener {

    private UHC uhc;

    public PracticeListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickUPItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {

                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = (Player) event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Golden Head")) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();


        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            event.setDeathMessage(null);
            event.getDrops().clear();

            if(killer != null) {
                //Killer
                UHCPlayer uhcKiller = uhc.getPlayerManager().getUhcPlayers().get(killer.getUniqueId());
                uhcKiller.setPracticeKills(uhcKiller.getPracticeKills() + 1);
                killer.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You killed " + player.getName());
                killer.getInventory().addItem(new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE).setName(ChatColor.GOLD + "Golden Head").build());
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You got killed by " + killer.getName());
            } else {
                //No Killer
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You died");
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.PRACTICE) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    uhcPlayer.setupPlayer();
                    uhcPlayer.setPracticeKills(0);

                    uhc.getInventories().loadPracticeHotBar(player);

                    player.teleport(uhc.getPlayerManager().getRandomLocation(uhc.getWorldManager().getUhc_practice(), uhc.getGameManager().getPracticeBorderSize()));
                }
            }.runTaskLater(uhc, 3);
        }
    }
}
