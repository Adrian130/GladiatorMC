package xyz.velium.uhc.listener.scenarios;

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.util.JavaUtils;

import java.util.*;

public class NoCleanListener implements Listener {

    private UHC uhc;

    public NoCleanListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Scenarios.NOCLEAN.isEnabled()) {
            if (uhc.getGameStates() == GameStates.INGAME) {

                for (UUID uuid : uhc.getScenarioManager().getNoClean()) {
                    if (Bukkit.getPlayer(uuid) != null) {
                        uhc.getScenarioManager().sendPacket(event.getPlayer(), Bukkit.getPlayer(uuid), true);
                        uhc.getScenarioManager().getPacketReceived().get(uuid).remove(event.getPlayer().getUniqueId());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Scenarios.NOCLEAN.isEnabled()) {
            if (uhc.getGameStates() == GameStates.INGAME) {
                for (UUID uuid : uhc.getScenarioManager().getNoClean()) {
                    if (Bukkit.getPlayer(uuid) != null) {
                        uhc.getScenarioManager().sendPacket(event.getPlayer(), Bukkit.getPlayer(uuid), false);
                        uhc.getScenarioManager().getPacketReceived().get(uuid).remove(event.getPlayer().getUniqueId());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (Scenarios.NOCLEAN.isEnabled()) {
            if (uhc.getGameStates() == GameStates.INGAME) {
                if (event.getEntity().getKiller() != null) {
                    uhc.getScenarioManager().giveNoClean(event.getEntity().getKiller());
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (Scenarios.NOCLEAN.isEnabled()) {
            if (uhc.getGameStates() == GameStates.INGAME) {
                if (event.getDamager() instanceof Player && uhc.getScenarioManager().getNoClean().contains(event.getDamager().getUniqueId())) {
                    uhc.getScenarioManager().removeNoClean((Player) event.getDamager());
                } else if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                    if (uhc.getScenarioManager().getNoClean().contains(event.getEntity().getUniqueId()))
                        ((Player) event.getDamager()).sendMessage(ChatColor.RED + "This player has NoClean");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (Scenarios.NOCLEAN.isEnabled()) {
            if (uhc.getGameStates() == GameStates.INGAME) {
                if (event.getEntity() instanceof Player && uhc.getScenarioManager().getNoClean().contains(event.getEntity().getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (Scenarios.NOCLEAN.isEnabled()) {
            if (uhc.getGameStates() == GameStates.INGAME) {
                if (event.getPlayer() != null && uhc.getScenarioManager().getNoClean().contains(event.getPlayer().getUniqueId())) {
                    uhc.getScenarioManager().removeNoClean(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (Scenarios.NOCLEAN.isEnabled()) {
            if (uhc.getGameStates() == GameStates.INGAME) {
                if (uhc.getScenarioManager().getNoClean().contains(event.getPlayer().getUniqueId())) {
                    if (event.getBucket() == Material.LAVA_BUCKET)
                        uhc.getScenarioManager().removeNoClean(event.getPlayer());
                }
            }
        }
    }
}
