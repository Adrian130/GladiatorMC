package xyz.velium.uhc.listener.gamestates;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class LobbyListener implements Listener {

    private UHC uhc;

    public LobbyListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickUPItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

            if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
                if(event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    uhcPlayer.teleportSpawn();
                    event.setCancelled(true);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if(uhc.getGameStates() == GameStates.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

            if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        ItemStack itemStack = event.getItem();
        Action action = event.getAction();

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            if(action == null) return;
            if(itemStack == null) return;
            if(itemStack.getItemMeta() == null) return;
            if(itemStack.getItemMeta().getDisplayName() == null) return;

            if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                switch (itemStack.getItemMeta().getDisplayName()) {
                    case "§bScenarios":
                        uhc.getInventories().openScenarioInventory(player);
                        break;

                    case "§dYour Team":
                        uhc.getInventories().openCurrentTeamInventory(player);
                        break;

                    case "§eConfiguration":
                        uhc.getInventories().openConfigInventory(player);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        ItemStack itemStack = event.getCurrentItem();

        if(uhcPlayer.getPlayerStates() == PlayerStates.LOBBY) {
            event.setCancelled(true);
        }
    }
}
