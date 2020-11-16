package xyz.velium.uhc.commands.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class StartCommand implements CommandExecutor {

    private UHC uhc;

    public StartCommand(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if(arguments.length == 0) {
                if(uhcPlayer.getPlayerStates() == PlayerStates.HOST || player.isOp()) {
                    if(!uhc.getStartTask().isStarted()) {
                        uhc.getStartTask().startScatterTask();
                    } else {
                        player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "The Game has already started");
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only Hosts can execute this command");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /start");
            }
        }
        return false;
    }
}
