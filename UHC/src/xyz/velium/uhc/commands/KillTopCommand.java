package xyz.velium.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

import java.util.*;

public class KillTopCommand implements CommandExecutor {

    private UHC uhc;

    public KillTopCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if (arguments.length == 0) {
                if (uhc.getGameStates() == GameStates.INGAME) {
                    player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Top 10 Players of this UHC");
                    player.sendMessage(" ");
                    Map<UUID, Integer> unsortedkills = new HashMap<>();

                    for (OfflinePlayer totalPlayers : Bukkit.getOfflinePlayers()) {
                        if(!uhc.getPlayerManager().getUhcPlayers().containsKey(totalPlayers.getUniqueId())) {
                            UHCPlayer uhcTotalPlayers = new UHCPlayer(totalPlayers.getUniqueId());
                            uhc.getPlayerManager().getUhcPlayers().put(totalPlayers.getUniqueId(), uhcTotalPlayers);
                        }
                        UHCPlayer uhcTotalPlayers = uhc.getPlayerManager().getUhcPlayers().get(totalPlayers.getUniqueId());
                        unsortedkills.put(totalPlayers.getUniqueId(), uhcTotalPlayers.getKills());
                    }
                    
                    Map<UUID, Integer> kills = sortByValue(unsortedkills);
                    
                    int topPlayers = 1;
                    for (Object object : kills.keySet()) {
                        if (topPlayers != 11) {
                            UUID uuid = (UUID) object;
                            if (kills.get(uuid).intValue() != 0) {
                                player.sendMessage(ChatColor.GOLD.toString() + topPlayers + ". " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(uuid).getName() + ": " + ChatColor.WHITE + kills.get(uuid));
                            }
                            topPlayers++;
                        } else {
                            break;
                        }
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "There is currently no game running");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /killtop");
            }
        }
        return false;
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
