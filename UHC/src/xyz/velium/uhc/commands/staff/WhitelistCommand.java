package xyz.velium.uhc.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

public class WhitelistCommand implements CommandExecutor {

    private UHC uhc;

    public WhitelistCommand(UHC uhc) {
        this.uhc = uhc;
    }

    private void sendWhitelistInformation(Player player) {
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Whitelist Commands");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + "/whitelist on " + ChatColor.GRAY + " - turn the Whitelist on");
        player.sendMessage(ChatColor.YELLOW + "/whitelist off " + ChatColor.GRAY + " - turn the Whitelist off");
        player.sendMessage(ChatColor.YELLOW + "/whitelist add <player> " + ChatColor.GRAY + " - add a player to the whitelist");
        player.sendMessage(ChatColor.YELLOW + "/whitelist remove <player> " + ChatColor.GRAY + " - remove a player from the whitelist");
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if(uhcPlayer.getPlayerStates() == PlayerStates.HOST || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR) {
                if(arguments.length == 1) {
                    if(arguments[0].equalsIgnoreCase("on")) {
                        uhc.getConfigManager().setWhitelist(true);
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Whitelist has been turned " + ChatColor.GREEN + "on");
                    } else if(arguments[0].equalsIgnoreCase("off")) {
                        uhc.getConfigManager().setWhitelist(false);
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Whitelist has been turned " + ChatColor.RED + "off");
                    } else {
                        this.sendWhitelistInformation(player);
                    }
                } else if(arguments.length == 2) {
                    if(arguments[0].equalsIgnoreCase("add")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(arguments[1]);
                        if(target != null) {
                            if(!uhc.getConfigManager().getWhitelistedPlayers().contains(target.getUniqueId())) {
                                uhc.getConfigManager().getWhitelistedPlayers().add(target.getUniqueId());
                                player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + " has been added to the Whitelist");
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + target.getName() + " is already whitelisted");
                            }
                        } else {
                            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[1] + " could not be found");
                        }
                    } else if(arguments[0].equalsIgnoreCase("remove")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(arguments[1]);
                        if(target != null) {
                            if(uhc.getConfigManager().getWhitelistedPlayers().contains(target.getUniqueId())) {
                                uhc.getConfigManager().getWhitelistedPlayers().remove(target.getUniqueId());
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + target.getName() + ChatColor.YELLOW + " has been removed from the Whitelist");
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + target.getName() + " is not whitelisted");
                            }
                        } else {
                            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[1] + " could not be found");
                        }
                    } else {
                        this.sendWhitelistInformation(player);
                    }
                } else {
                    this.sendWhitelistInformation(player);
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only Hosts can execute this command");
            }
        }
        return false;
    }
}
