package xyz.velium.uhc.manager;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.util.JavaUtils;

import java.util.*;

@Getter @Setter
public class ScenarioManager {

    private UHC uhc;

    public ScenarioManager(UHC uhc) {
        this.uhc = uhc;
    }

    private HashMap<UUID, Integer> noCleanTime = new HashMap<>();
    private HashSet<UUID> noClean = new HashSet<>();
    private HashMap<UUID, Set<UUID>> packetReceived = new HashMap<>(); 

    private int noCleanTask;

    public void giveNoClean(Player player) {
        if (player == null) return;
        player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You're now invincible for 20 seconds");
        this.noClean.add(player.getUniqueId());
        this.noCleanTime.put(player.getUniqueId(), 20);
        if (!this.packetReceived.containsKey(player.getUniqueId()))
            this.packetReceived.put(player.getUniqueId(), new HashSet<>());
        //update name
        for (Player online : uhc.getOnlinePlayers()) {
            sendPacket(online, player, true);
            this.packetReceived.get(player.getUniqueId()).remove(online.getUniqueId());
        }
    }

    public void removeNoClean(Player player) {
        if (!noClean.contains(player.getUniqueId())) return;
        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You're no longer invincible");
        //update name
        for (Player online : uhc.getOnlinePlayers()) {
            sendPacket(online, player, false);
            this.packetReceived.get(player.getUniqueId()).remove(online.getUniqueId());
        }
        this.noClean.remove(player.getUniqueId());
        this.noCleanTime.remove(player.getUniqueId());
    }

    public void sendPacket(Player forPlayer, Player player, boolean create) {
        ((CraftPlayer) forPlayer).getHandle().playerConnection.sendPacket(getPacket(player, create));
    }

    public PacketPlayOutScoreboardTeam getPacket(Player player, boolean create) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        JavaUtils.setField(packet, "a", player.getName());
        JavaUtils.setField(packet, "b", "");
        JavaUtils.setField(packet, "c", ChatColor.GOLD.toString());
        JavaUtils.setField(packet, "d", "");
        JavaUtils.setField(packet, "e", Collections.singletonList(player.getName()));
        JavaUtils.setField(packet, "g", 1);
        JavaUtils.setField(packet, "f", create ? 0 : 1);
        return packet;
    }
    
    public void startNoCleanTask() {

        this.noCleanTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, new BukkitRunnable() {
            @Override
            public void run() {
                for(Player players : uhc.getOnlinePlayers()) {
                    if(noClean.contains(players.getUniqueId())) {
                        noCleanTime.put(players.getUniqueId(), noCleanTime.get(players.getUniqueId()) -1);
                        if(noCleanTime.get(players.getUniqueId()) == 0) {
                            removeNoClean(players);
                        }
                    } else if(!noClean.contains(players.getUniqueId())) {
                        if(noCleanTime.containsKey(players.getUniqueId())) {
                            noCleanTime.remove(players.getUniqueId());
                        }
                    }
                }
            }
        }, 20, 20);
    }
}
