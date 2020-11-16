package xyz.velium.uhc.listener.gamestates;

import net.minecraft.server.v1_7_R4.Village;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameListener implements Listener {

    private final UHC uhc;

    public GameListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());


        if (uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
            if (!uhc.getConfigManager().isPvp()) {
                if (event.getBucket().equals(Material.LAVA_BUCKET)) {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t place lava before pvp");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (uhc.getGameStates() == GameStates.INGAME) {
            if (!uhc.getConfigManager().isPvp()) {
                switch (event.getBlockPlaced().getType()) {
                    case FIRE:
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t place fire before pvp");
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageBow(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (uhc.getGameStates() == GameStates.INGAME) {
                if(uhc.getConfigManager().isPvp()) {
                    if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() > 0) {
                        ((Player) ((Arrow) event.getDamager()).getShooter()).sendMessage(uhc.getPREFIX() + ChatColor.RED + player.getName() + ChatColor.YELLOW + " is currently on " + ChatColor.DARK_RED.toString() + Math.ceil(((CraftPlayer) player).getHealth() / 2) + "♥");
                    }
                } else { //IPvP Bow
                    event.setCancelled(true);
                }
            }
        } else if (event.getDamager() instanceof FishHook && event.getEntity() instanceof Player) {
            if (uhc.getGameStates() == GameStates.INGAME) {
                if (!uhc.getConfigManager().isPvp()) {
                    event.setCancelled(true);
                }
            }
        } else if(event.getEntity() instanceof Villager) {
            Villager villager = (Villager) event.getEntity();
            if(villager.getCustomName() != null) {
                if(!uhc.getConfigManager().isPvp()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (!uhc.getConfigManager().isEnderpearlDamage()) {
                player.teleport(event.getTo());
                event.setCancelled(true);
            } else {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        if (uhc.getGameStates() == GameStates.INGAME) {
            if (event.getRecipe().getResult().getType() == Material.GOLDEN_APPLE) {
                if (!uhc.getConfigManager().isGodApples()) {
                    if (event.getRecipe().getResult().getDurability() == 1) {
                        event.getInventory().setResult(new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(damager.getUniqueId());
            if (uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                if (!uhc.getConfigManager().isPvp()) {
                    if (event.getEntity() instanceof Player) {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(false);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
            if (event.getEntity() instanceof Villager) {
                Villager villager = (Villager) event.getEntity();

                if(villager.getCustomName() != null) {
                    String splitter = " ";
                    String[] split = villager.getCustomName().split(splitter);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(split[0]);
                    Location location = villager.getLocation();
                    location.getBlock().setType(Material.NETHER_FENCE);
                    Location locationAbove = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
                    Block block = locationAbove.getBlock();
                    block.setType(Material.SKULL);
                    Skull skull = (Skull) block.getState();
                    skull.setSkullType(SkullType.PLAYER);
                    skull.setOwner(split[0]);
                    skull.setRawData((byte) 1);
                    skull.update();

                    UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(offlinePlayer.getUniqueId());
                    uhcPlayer.setPlayerStates(PlayerStates.SPECTATOR);
                    uhcPlayer.dropPlayerDeathInventory();
                    uhc.getPlayerManager().getAlivePlayers().remove(uhcPlayer.getUuid());
                    uhc.getPlayerManager().getLogoutPlayers().remove(uhcPlayer.getUuid());

                    if (uhc.getTeamManager().isTeams()) {
                        Team team = uhcPlayer.getTeam();
                        if (team != null) {

                            team.getAlivePlayers().remove(uhcPlayer.getUuid());

                            if (team.getAlivePlayers().size() == 0) {
                                uhc.getTeamManager().getAliveTeams().remove(team);
                            }
                        }
                    }

                    uhc.getGameManager().checkWin();

                    if(event.getEntity().getKiller() != null) {
                        Player player = event.getEntity().getKiller();
                        UHCPlayer uhcKiller = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
                        uhcKiller.setKills(uhcKiller.getKills() + 1);
                        uhcKiller.getKilledPlayers().add(villager.getCustomName());
                        Bukkit.broadcastMessage(ChatColor.RED + villager.getCustomName() + ChatColor.GRAY + "[" + ChatColor.WHITE + uhcPlayer.getKills() + ChatColor.GRAY + "] " + " (Combat Logger) " + ChatColor.YELLOW + "was slain by " + ChatColor.GREEN + player.getName() + ChatColor.GRAY + "[" + ChatColor.WHITE + uhcKiller.getKills() + ChatColor.GRAY + "]");
                    } else if(event.getEntity().getKiller() == null) {
                        Player player = event.getEntity().getKiller();
                        Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getOfflinePlayer(uhcPlayer.getUuid()).getName() + ChatColor.GRAY + "[" + ChatColor.WHITE + uhcPlayer.getKills() + ChatColor.GRAY + "] " + ChatColor.YELLOW + "died " + ChatColor.GRAY + "(Combat Logger)");
                    }
                }
            }
        }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        //Skull Fence
        if(!Scenarios.TIMEBOMB.isEnabled()) {
            String splitter = " ";
            String[] split = player.getName().split(splitter);
            Location location = player.getLocation();
            location.getBlock().setType(Material.FENCE);
            Location locationAbove = new Location(location.getWorld(), location.getX(), location.getY() + 1.0D, location.getZ());
            Block block = locationAbove.getBlock();
            block.setType(Material.SKULL);
            Skull skull = (Skull) block.getState();
            skull.setSkullType(SkullType.PLAYER);
            skull.setOwner(split[0]);
            skull.setRawData((byte) 1);
            skull.update();
        }

        if (uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
            if (killer != null) {
                UHCPlayer uhcKiller = uhc.getPlayerManager().getUhcPlayers().get(killer.getUniqueId());
                if (uhc.getTeamManager().isTeams()) {
                    if (uhcKiller.getTeam() != null) {
                        Team team = uhcKiller.getTeam();
                        team.setTeamKills(team.getTeamKills() + 1);
                    }
                }
                uhcKiller.setKills(uhcKiller.getKills() + 1);
                uhcKiller.getKilledPlayers().add(player.getName());
                event.setDeathMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + "[" + ChatColor.WHITE + uhcPlayer.getKills() + ChatColor.GRAY + "] " + ChatColor.YELLOW + "was slain by " + ChatColor.GREEN + killer.getName() + ChatColor.GRAY + "[" + ChatColor.WHITE + uhcKiller.getKills() + ChatColor.GRAY + "]");
            } else {
                event.setDeathMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + "[" + ChatColor.WHITE + uhcPlayer.getKills() + ChatColor.GRAY + "] " + ChatColor.YELLOW + "died");
            }

            uhcPlayer.handleDeath();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());


        if (uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
            if (block.getType() == Material.DIAMOND_ORE) {
                uhcPlayer.setMinedDiamonds(uhcPlayer.getMinedDiamonds() + 1);
                if (uhcPlayer.getMinedDiamonds() == 5 || uhcPlayer.getMinedDiamonds() == 10 || uhcPlayer.getMinedDiamonds() == 15 || uhcPlayer.getMinedDiamonds() == 20 || uhcPlayer.getMinedDiamonds() == 25 || uhcPlayer.getMinedDiamonds() == 30 || uhcPlayer.getMinedDiamonds() == 35 || uhcPlayer.getMinedDiamonds() == 40 || uhcPlayer.getMinedDiamonds() == 45 || uhcPlayer.getMinedDiamonds() == 50 || uhcPlayer.getMinedDiamonds() >= 50) {
                    for (Player onlinePlayers : uhc.getOnlinePlayers()) {
                        UHCPlayer onlineUHCPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
                        if (onlineUHCPlayers.isStaffNotifications()) {
                            if (onlineUHCPlayers.getPlayerStates() == PlayerStates.HOST || onlineUHCPlayers.getPlayerStates() == PlayerStates.MODERATOR) {
                                onlinePlayers.sendMessage(uhc.getPREFIX() + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.RED + " has mined " + ChatColor.AQUA + uhcPlayer.getMinedDiamonds() + " Diamonds!");
                            }
                        }
                    }
                }
            } else if (block.getType() == Material.GOLD_ORE) {
                uhcPlayer.setMinedGold(uhcPlayer.getMinedGold() + 1);
                if (uhcPlayer.getMinedGold() == 45 || uhcPlayer.getMinedGold() == 50 || uhcPlayer.getMinedGold() == 55 || uhcPlayer.getMinedGold() == 60 || uhcPlayer.getMinedGold() == 65 || uhcPlayer.getMinedGold() >= 70) {
                    for (Player onlinePlayers : uhc.getOnlinePlayers()) {
                        UHCPlayer onlineUHCPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
                        if (onlineUHCPlayers.isStaffNotifications()) {
                            if (onlineUHCPlayers.getPlayerStates() == PlayerStates.HOST || onlineUHCPlayers.getPlayerStates() == PlayerStates.MODERATOR) {
                                onlinePlayers.sendMessage(uhc.getPREFIX() + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.RED + " has mined " + ChatColor.GOLD + uhcPlayer.getMinedGold() + " Gold!");
                            }
                        }
                    }
                }
            } else if (block.getType() == Material.MOB_SPAWNER) {
                uhcPlayer.setMinedSpaners(uhcPlayer.getMinedSpaners() + 1);
                for (Player onlinePlayers : uhc.getOnlinePlayers()) {
                    UHCPlayer onlineUHCPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
                    if (onlineUHCPlayers.isStaffNotifications()) {
                        if (onlineUHCPlayers.getPlayerStates() == PlayerStates.HOST || onlineUHCPlayers.getPlayerStates() == PlayerStates.MODERATOR) {
                            onlinePlayers.sendMessage(uhc.getPREFIX() + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.RED + " has mined " + ChatColor.DARK_RED + uhcPlayer.getMinedSpaners() + " Spawners!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    uhcPlayer.addSpectator();
                }
            }.runTaskLater(uhc, 3);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if(uhc.getGameStates() == GameStates.INGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        if (uhc.getGameStates() == GameStates.INGAME) {
            if (event.getContents().getIngredient().getType() == Material.SUGAR && !uhc.getConfigManager().isSpeed1()) {
                event.setCancelled(true);
            }


            if (event.getContents().getIngredient().getType() == Material.BLAZE_POWDER && !uhc.getConfigManager().isStrenght1()) {
                event.setCancelled(true);
            }


            if (event.getContents().getIngredient().getType() == Material.SPIDER_EYE) {
                event.setCancelled(true);
            }

            List<ItemStack> potions = new ArrayList<ItemStack>();
            potions.addAll(Arrays.asList(event.getContents().getContents()));
            for (ItemStack itemStack : potions) {
                if (itemStack.getType() == Material.POTION) {
                    Potion potion = Potion.fromItemStack(itemStack);
                    if (potion.getType() == PotionType.SPEED && potion.getLevel() == 1 && !uhc.getConfigManager().isSpeed2()) {
                        event.setCancelled(true);
                    }


                    if (potion.getType() == PotionType.STRENGTH && potion.getLevel() == 1 && !uhc.getConfigManager().isStrenght2()) {
                        event.setCancelled(true);
                    }


                    if (potion.getType() == PotionType.POISON && potion.getLevel() == 1) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (uhc.getGameStates() == GameStates.INGAME) {
            if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Golden Head")) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEnttiy(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if(event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) event.getRightClicked();
            if(villager.getCustomName() != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        if (uhc.getGameStates() == GameStates.INGAME) {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL && event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_world()) && uhc.getGameManager().getBorderSize() <= 500) {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t go to the nether anymore");
                event.setCancelled(true);
            } else {
                if (event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_world())) {

                    double x = player.getLocation().getX() / 8;
                    double y = player.getLocation().getY() / 8;
                    double z = player.getLocation().getZ() / 8;

                    Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_nether()), x, y, z);
                    event.setTo(event.getPortalTravelAgent().findOrCreate(location));

                } else if (event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_nether())) {

                    double x = player.getLocation().getX() * 8;
                    double y = player.getLocation().getY() * 8;
                    double z = player.getLocation().getZ() * 8;

                    Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_world()), x, y, z);
                    event.setTo(event.getPortalTravelAgent().findOrCreate(location));
                }
            }
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL && event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_world())) {
                if (!uhc.getConfigManager().isNether()) {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Nether is disabled");
                    event.setCancelled(true);
                }
            } else {
                if (event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_world())) {

                    double x = player.getLocation().getX() / 8;
                    double y = player.getLocation().getY() / 8;
                    double z = player.getLocation().getZ() / 8;

                    Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_nether()), x, y, z);
                    event.setTo(event.getPortalTravelAgent().findOrCreate(location));

                } else if (event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_nether())) {

                    double x = player.getLocation().getX() * 8;
                    double y = player.getLocation().getY() * 8;
                    double z = player.getLocation().getZ() * 8;

                    Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_world()), x, y, z);
                    event.setTo(event.getPortalTravelAgent().findOrCreate(location));
                }
            }
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL && event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_world())) {
                if (!uhc.getConfigManager().isPvp()) {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t go to the nether before pvp");
                    event.setCancelled(true);
                } else {
                    if (event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_world())) {

                        double x = player.getLocation().getX() / 8;
                        double y = player.getLocation().getY() / 8;
                        double z = player.getLocation().getZ() / 8;

                        Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_nether()), x, y, z);
                        event.setTo(event.getPortalTravelAgent().findOrCreate(location));

                    } else if (event.getFrom().getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_nether())) {

                        double x = player.getLocation().getX() * 8;
                        double y = player.getLocation().getY() * 8;
                        double z = player.getLocation().getZ() * 8;

                        Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_world()), x, y, z);
                        event.setTo(event.getPortalTravelAgent().findOrCreate(location));
                    }
                }
            }
        }
    }
}
