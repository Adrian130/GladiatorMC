package xyz.velium.uhc.commands.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class ToggleStaffNotificationsCommand implements CommandExecutor {

    private UHC uhc;

    public ToggleStaffNotificationsCommand(UHC uhc) {
        this.uhc = uhc;
    }

    private void handleStaffNotifications(Player player) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if (uhcPlayer.isStaffNotifications()) {
            uhcPlayer.setStaffNotifications(false);
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You have disabled your Staff Notifications");
        } else if (!uhcPlayer.isStaffNotifications()) {
            uhcPlayer.setStaffNotifications(true);
            player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You have enabled your Staff Notifications");
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if (arguments.length == 0) {
                if (uhcPlayer.getPlayerStates() == PlayerStates.HOST || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR) {
                    this.handleStaffNotifications(player);
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only Moderators can execute this Command");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /staffnotifications");
            }
        }
        return false;
    }
}
