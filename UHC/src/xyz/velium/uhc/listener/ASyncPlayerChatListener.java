package xyz.velium.uhc.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class ASyncPlayerChatListener implements Listener {

    private final UHC uhc;

    public ASyncPlayerChatListener(UHC uhc) {
        this.uhc = uhc;
    }

    private void sendSpecChatMessage(Player player, String message) {
        for (Player onlinePlayer : uhc.getOnlinePlayers()) {
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayer.getUniqueId());
            if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
                onlinePlayer.sendMessage(ChatColor.GRAY + "[SpecChat] " + player.getName() + ": " + ChatColor.WHITE + message);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onASyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        message = message.replace("%", "%%");

        if (uhcPlayer.getPlayerStates() == PlayerStates.HOST) {
            if (event.getMessage().contains("-spec")) {
                this.sendSpecChatMessage(player, message.replace("-spec", ""));
                event.setCancelled(true);
                return;
            } else {
                event.setFormat(ChatColor.DARK_RED + "[Host] " + event.getFormat());
            }
        }

        if (uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR) {
            if (event.getMessage().contains("-spec")) {
                this.sendSpecChatMessage(player, message.replace("-spec", ""));
                event.setCancelled(true);
                return;
            } else {
                event.setFormat(ChatColor.DARK_AQUA + "[UHC-Mod] " + event.getFormat());
            }
        }

        if (uhcPlayer.getPlayerStates() == PlayerStates.SPECTATOR) {
            event.setCancelled(true);
            this.sendSpecChatMessage(player, message.replace("-spec", ""));
            return;
        }

        if (uhc.getTeamManager().isTeams()) {
            if (uhcPlayer.getTeam() != null) {
                event.setFormat(ChatColor.YELLOW + "[#" + uhcPlayer.getTeam().getTeamNumber() + "] " + event.getFormat());
            }
        }
    }
}
