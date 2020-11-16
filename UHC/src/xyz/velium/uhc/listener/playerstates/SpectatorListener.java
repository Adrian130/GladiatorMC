package xyz.velium.uhc.listener.playerstates;

import org.bukkit.*;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class SpectatorListener implements Listener {

    private UHC uhc;

    public SpectatorListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickUPItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

            if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

            if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if(event.getAttacker() instanceof Player) {
            Player player = (Player) event.getAttacker();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if(event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
                event.setCollisionCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        ItemStack itemStack = event.getItem();
        Action action = event.getAction();

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {

            if(action == Action.RIGHT_CLICK_BLOCK) {
                if(event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST) || event.getClickedBlock().getType().equals(Material.STORAGE_MINECART) ||event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    return;
                }
            }

            if (action == null) return;
            if (itemStack == null) return;
            if (itemStack.getItemMeta() == null) return;
            if (itemStack.getItemMeta().getDisplayName() == null) return;

            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                switch (itemStack.getItemMeta().getDisplayName()) {
                    case "§eAlive Players":
                        uhc.getInventories().openAlivePlayersInventory(player);
                        break;
                    case "§6Teleport 0,0":
                        player.teleport(new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_world()), 0, Bukkit.getWorld(uhc.getWorldManager().getUhc_world()).getHighestBlockYAt(0, 0) + 2,0));
                        break;
                    case "§bMining Players":
                        uhc.getInventories().openMiningPlayersInventory(player);
                        break;
                    case "§cNether Players":
                        uhc.getInventories().openNetherPlayersInventory(player);
                        break;
                    case "§3Worlds":
                        uhc.getInventories().openWorldInventory(player);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
             new BukkitRunnable() {
                 @Override
                 public void run() {
                     player.setGameMode(GameMode.CREATIVE);
                 }
             }.runTaskLater(uhc, 5);
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        ItemStack itemStack = player.getItemInHand();

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {

            if (itemStack == null) return;
            if (itemStack.getItemMeta() == null) return;
            if (itemStack.getItemMeta().getDisplayName() == null) return;

            if (event.getRightClicked() instanceof Player) {
                if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "View Inventory")) {
                    uhc.getInventories().openViewInventory(player, (Player) event.getRightClicked());
                } else if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Player Information")) {
                    uhc.getInventories().openPlayerInformation(player, (Player) event.getRightClicked());
                }
            } else if (event.getRightClicked() instanceof Horse) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        ItemStack itemStack = event.getCurrentItem();
        Inventory inventory = event.getInventory();

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            if(inventory.getType() == InventoryType.PLAYER || inventory.getType() == InventoryType.CREATIVE) {
                event.setCancelled(true);
                return;
            }

            if(inventory.getName().equalsIgnoreCase(ChatColor.GREEN + "Inventory")) {
                event.setCancelled(true);
            }

            if (inventory == null) return;
            if (itemStack == null) return;
            if (itemStack.getItemMeta() == null) return;
            if (itemStack.getItemMeta().getDisplayName() == null) return;

            if (inventory.getName().equalsIgnoreCase(ChatColor.YELLOW + "Alive Players") || inventory.getName().equalsIgnoreCase(ChatColor.AQUA + "Mining Players") || inventory.getName().equalsIgnoreCase(ChatColor.RED + "Nether Players")) {
                Player targetPlayer = Bukkit.getPlayer(itemStack.getItemMeta().getDisplayName());
                if (targetPlayer != null) {
                    player.teleport(targetPlayer);
                    player.sendMessage(uhc.getPREFIX() + ChatColor.YELLOW + "You have been teleported to " + ChatColor.GRAY + targetPlayer.getName());
                    event.setCancelled(true);
                } else {
                    player.sendMessage(ChatColor.RED + itemStack.getItemMeta().getDisplayName() + " could not be found");
                    event.setCancelled(true);
                }
            } else if (inventory.getName().equalsIgnoreCase(ChatColor.DARK_AQUA + "Worlds")) {
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_AQUA + "Game World")) {
                    player.teleport(Bukkit.getWorld(uhc.getWorldManager().getUhc_world()).getSpawnLocation());
                    player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You have been teleported to the Game World spawn");
                    event.setCancelled(true);
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_AQUA + "Nether World")) {
                    player.teleport(Bukkit.getWorld(uhc.getWorldManager().getUhc_nether()).getSpawnLocation());
                    player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You have been teleported to the Nether spawn");
                    event.setCancelled(true);
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_AQUA + "Lobby World")) {
                    player.teleport(Bukkit.getWorld(uhc.getWorldManager().getLobby_world()).getSpawnLocation());
                    player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You have been teleported to the Lobby spawn");
                    event.setCancelled(true);
                }
            }
        }
    }
}
